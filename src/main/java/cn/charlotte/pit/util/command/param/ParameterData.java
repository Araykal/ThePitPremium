package cn.charlotte.pit.util.command.param;

public final class ParameterData {

    private final String name;
    private final boolean wildcard;
    private final String defaultValue;
    private final String[] tabCompleteFlags;
    private final Class<?> parameterClass;

    public ParameterData(Parameter parameterAnnotation, Class<?> parameterClass) {
        this.name = parameterAnnotation.name();
        this.wildcard = parameterAnnotation.wildcard();
        this.defaultValue = parameterAnnotation.defaultValue();
        this.tabCompleteFlags = parameterAnnotation.tabCompleteFlags();
        this.parameterClass = parameterClass;
    }

    public String getName() {
        return this.name;
    }

    public boolean isWildcard() {
        return this.wildcard;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String[] getTabCompleteFlags() {
        return this.tabCompleteFlags;
    }

    public Class<?> getParameterClass() {
        return this.parameterClass;
    }
}