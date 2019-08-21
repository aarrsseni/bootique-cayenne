package io.bootique.cayenne.test.experiment;

import java.util.ArrayList;
import java.util.Collection;

import io.bootique.BQRuntime;
import io.bootique.command.CommandOutcome;
import io.bootique.test.junit.BQTestRuntimeBuilder;

public class BQTestFactory {
    private Collection<BQRuntime> runtimes;
    private boolean autoLoadModules;

    public BQTestFactory() {
        this.runtimes = new ArrayList<>();
    }

    public BQTestFactory autoLoadModules() {
        this.autoLoadModules = true;
        return this;
    }

    public BQTestFactory.Builder app(String... args) {
        BQTestFactory.Builder builder = new BQTestFactory.Builder(this.runtimes, args);
        if (this.autoLoadModules) {
            builder.autoLoadModules();
        }

        return builder;
    }

    public static class Builder extends BQTestRuntimeBuilder<BQTestFactory.Builder> {
        private Collection<BQRuntime> runtimes;

        private Builder(Collection<BQRuntime> runtimes, String[] args) {
            super(args);
            this.runtimes = runtimes;
        }

        public BQRuntime createRuntime() {
            BQRuntime runtime = this.bootique.createRuntime();
            this.runtimes.add(runtime);
            return runtime;
        }

        public CommandOutcome run() {
            return this.createRuntime().run();
        }
    }
}
