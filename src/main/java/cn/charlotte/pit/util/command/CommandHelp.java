package cn.charlotte.pit.util.command;

public class CommandHelp {

    private String syntax;
    private String description;

    public CommandHelp(String syntax, String description) {
        this.syntax = syntax;
        this.description = description;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
