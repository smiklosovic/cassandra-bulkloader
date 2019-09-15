package com.instaclustr.cassandra.bulkloader;

import java.io.PrintWriter;
import java.util.concurrent.Callable;

import com.instaclustr.cassandra.bulkloader.loader.CSVBulkLoader;
import com.instaclustr.cassandra.bulkloader.loader.RandomBulkLoader;
import com.instaclustr.picocli.JarManifestVersionProvider;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(subcommands = {CSVBulkLoader.class, RandomBulkLoader.class},
         synopsisSubcommandLabel = "COMMAND",
         versionProvider = CLIApplication.class
)
public class CLIApplication extends JarManifestVersionProvider implements Runnable {

    @Option(names = {"-V", "--version"},
            versionHelp = true,
            description = "print version information and exit")
    private boolean version;

    @Spec
    private CommandSpec spec;

    @Override
    public String getImplementationTitle() {
        return "bulkloader";
    }

    public static void main(String[] args) {
        System.exit(execute(new CommandLine(new CLIApplication()), args));
    }

    public static int execute(final Runnable runnable, String... args) {
        return execute(new CommandLine(runnable), args);
    }

    public static int execute(final Callable callable, String... args) {
        return execute(new CommandLine(callable), args);
    }

    public static int execute(CommandLine commandLine, String... args) {
        return commandLine
                .setErr(new PrintWriter(System.err))
                .setOut(new PrintWriter(System.err))
                .setUnmatchedArgumentsAllowed(true)
                .setColorScheme(new CommandLine.Help.ColorScheme.Builder().ansi(CommandLine.Help.Ansi.ON).build())
                .setExecutionExceptionHandler((ex, cmdLine, parseResult) -> {

                    ex.printStackTrace();

                    return 1;
                })
                .execute(args);
    }

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand.");
    }
}
