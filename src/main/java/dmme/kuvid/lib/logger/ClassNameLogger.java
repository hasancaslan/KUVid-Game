package dmme.kuvid.lib.logger;

import dmme.kuvid.constants.LogLevel;
import java.time.Instant;

/**
 * Time, thread, class name logger with certain log level
 *
 * @author Hasan Can
 */
public class ClassNameLogger extends Logger {
    private final String name;

    public ClassNameLogger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    public ClassNameLogger(Class<?> clazz, LogLevel level) {
        super(level);
        this.name = clazz.getSimpleName();
    }

    @Override
    protected void write(String level, String message) {
        System.out.println(String.format("[%s] [%s] [%s] [%s] %s",
                Instant.now(),
                Thread.currentThread().getName(),
                name,
                level,
                message));
    }
}