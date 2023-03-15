package mouserun.functions;

/**
 *
 * @author josema
 * @param <U> First field (key) in a Pair
 * @param <V> Second field (value) in a Pair
 */
// Pair class
public class Pair<U, V> {

    public final U first;       // el primer campo de un par
    public final V second;      // el segundo campo de un par

    // Construye un nuevo par con valores especificados
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    // Verifica que el objeto especificado sea "igual a" el objeto actual o no
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        // llamar al método `equals()` de los objetos subyacentes
        if (!first.equals(pair.first)) {
            return false;
        }
        return second.equals(pair.second);
    }

    @Override
    // Calcula el código hash de un objeto para admitir tablas hash
    public int hashCode() {
        // usa códigos hash de los objetos subyacentes
        return 31 * first.hashCode() + second.hashCode();
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}
