package src.analyzers.semantic;

public class Symbol implements Cloneable {

    public static int getNextAvailableAddressAndUpdate(int allocatedSize) {
        int availableAddress = nextAvailableAddress;
        nextAvailableAddress += allocatedSize;

        return availableAddress;
    }
    public static int nextAvailableAddress = 1;
    public static int nextAvailableLabel = 1;
    //FIXME endereço 0 reservado para retorno

    public String identifier;
    public SymbolType type;
    public boolean localScope;
    public int address;

    //FIXME dentro de uma funcao, atribuição de funcao do lado esquerdo (func := 4) so permitido se essa funcao for a funcao atual

    public boolean equivalentTypeTo(SymbolType otherType) {
        return switch (type) {
            case VARIAVEL_INTEIRO, FUNCAO_INTEIRO -> SymbolType.integers.contains(otherType);
            case VARIAVEL_BOOLEANO, FUNCAO_BOOLEANO -> SymbolType.booleans.contains(otherType);

            default -> type == otherType;
        };
    }

    public Symbol(String identifier, SymbolType type, Boolean localScope) {
        this.identifier = identifier;
        this.type = type;
        this.localScope = localScope;
    }

    public Symbol(String identifier, SymbolType type) {
        this(identifier, type, false);
    }
    public Symbol(String identifier) {
        this(identifier, null);
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

    public void setAddress(int address) {
        this.address = address;
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

    @Override
    public String toString() {
        return String.format("{%s | %s | %s | 0x%d}", identifier, type, localScope, address );
    }

    @Override
    public Symbol clone() {
        try {
            return (Symbol) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
