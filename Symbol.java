public class Symbol {
    private String identifier;
    private SymbolType type;
    private Boolean localScope;


    public Symbol(String identifier, SymbolType type, Boolean localScope) {
        this.identifier = identifier;
        this.type = type;
        this.localScope = localScope;
    }

    public Symbol(String identifier, SymbolType type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public Boolean getLocalScope() {
        return localScope;
    }

    public void setLocalScope(Boolean localScope) {
        this.localScope = localScope;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode() + type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
