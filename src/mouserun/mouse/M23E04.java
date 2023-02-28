package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;

/**
 * Clase que contiene el esqueleto del raton base para las prácticas de
 * Inteligencia Artificial del curso 2020-21.
 *
 * @author Cristóbal José Carmona (ccarmona@ujaen.es)
 */
public class M23E04 extends Mouse {

    /**
     * Variable para almacenar la ultima celda visitada
     */
    private Grid lastGrid;

    /**
     * Variable para guardar el anterior movimiento realizado
     */
    private int movAnterior;

    /**
     * Tabla hash para almacenar las celdas visitadas por el raton:
     * Clave:Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;

    /**
     * Pila para almacenar el camino recorrido.
     */
    private final Stack<Grid> pilaMovimientos;

    /**
     * Constructor (Puedes modificar el nombre a tu gusto).
     */
    public M23E04() {
        //Le pasamos el nombre
        super("Gareth BALE");
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
    }

    /**
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        //region Almacenamiento de la posicion
            //Obtenemos la posicion x de la celda en la que se encuentra
            Integer x = currentGrid.getX();
            //Obtenemos la posicion y de la celda en la que se encuentra
            Integer y = currentGrid.getY();
            //Clase Pair le mandamos las posiciones obtenidas de la celda actual
            Pair posiCelda = new Pair<>(x,y);
        //endregion
        //region Comprueba celda Visitada()
            compruebaCeldaVisit(posiCelda,currentGrid);
        //endregion
        //region Comprobador casillas adyacentes a  la casilla actual (Posibles Movimientos)
        //Inicializamos un Arraylist para almacenar los Posibles Movimientos
        ArrayList<Integer> movimientos = new ArrayList<>();
                posiblesMoves(x,y,currentGrid,movimientos);
        //endregion
        //region Chequeador de movimientos()
            //Si el Arraylist de Movimientos no esta vacío añadimos el primer movimiento
            if(!movimientos.isEmpty()){
                //Añadimos la Casilla actual  a la pila de movimientos
                pilaMovimientos.add(currentGrid);
                //Devolvemos el primero movimiento hecho
                return movimientos.get(0);
            }
            //Si el Arraylist de Posible de Movimientos  (esta vacío) significa que está encerrado
            else{
                if(!pilaMovimientos.isEmpty()){
                    return  ultimoMov(x,y);
                }
                else{
                    return  inicio(currentGrid);
                }

            }
        //endregion

    }
    /**
     * @brief Método que comprueba los posibles movimientos a partir de su posicion actual
     *
     */
    private void posiblesMoves(Integer x, Integer y, Grid currentGrid , ArrayList<Integer> movimientos) {

        //region Casos de posibles movimientos
            //Caso 1 (En caso de que vaya hacia arriba "y+1" )
            Pair celdaSuperior = new Pair(x,y+1);
            //Comprobamos si la casilla actual puede ir hacia arriba
            //Y si no contiene la celda de arriba
            if(currentGrid.canGoUp() && !celdasVisitadas.containsKey(celdaSuperior)){
                //Añadimos el movimiento hacia arriba
                movimientos.add(Mouse.UP);
            }
            //Caso 2 (En caso de que vaya hacia abajo "y-1" )
            Pair celdaInferior = new Pair(x,y-1);
            //Comprobamos si la casilla actual puede ir hacia abajo
            //Y si no contiene la celda de abajo
            if(currentGrid.canGoDown() && !celdasVisitadas.containsKey(celdaInferior)){
                //Añadimos el movimiento hacia a abajo
                movimientos.add(Mouse.DOWN);
            }
            //Caso 3 (En caso de que vaya hacia la derecha  "x+1" )
            Pair celdaDerecha = new Pair(x+1,y);
            //Comprobamos si la casilla actual puede ir hacia derecha
            //Y si no contiene la celda de derecha
            if(currentGrid.canGoRight() && !celdasVisitadas.containsKey(celdaDerecha)){
                //Añadimos el movimiento hacia la derecha
                movimientos.add(Mouse.RIGHT);
            }
            //Caso 4 (En caso de que vaya hacia la izquierda  "x-1" )
            Pair celdaIzquierda = new Pair(x-1,y);
            //Comprobamos si la casilla actual puede ir hacia izquierda
            //Y si no contiene la celda de izquierda
            if(currentGrid.canGoLeft() && !celdasVisitadas.containsKey(celdaIzquierda)){
                //Añadimos el movimiento hacia la izquierda
                movimientos.add(Mouse.LEFT);
            }
        //endregion
    }

    /**
     * @brief Método que comprueba la celda visitada
     *
     */
    private void compruebaCeldaVisit(Pair posiCelda, Grid currentGrid) {
        //Comprobamos si celda visitada ha sido visitada.
        //Caso no haber sido visitada
        if(!celdasVisitadas.containsKey(posiCelda)){
            //Lo metemos en el HashMap  celdas vistadas
            celdasVisitadas.put(posiCelda,currentGrid);
            //LLamamos al metodo de clase que hace que incremente el numero de celdas visitadas
            //Para asi tener un conteo de ellas
            this.incExploredGrids();
        }
    }

    /**
     * @brief Método que se llama cuando la pila de movimientos está vacia
     *        y tambien esta vacia la lista de posibles movimientos
     *
     */
    private Integer inicio(Grid currentGrid) {
        //Inicializamos las celdas Adyancentes
        ArrayList<Integer> celdasAdyacentes = new ArrayList<>();
        //Comprobamos los posibles casos de movimientos
        //Caso1 Puede mover para arriba
        if (currentGrid.canGoUp()){
            celdasAdyacentes.add(Mouse.UP);
        }
        //Caso2 Puede mover para abajo
        if (currentGrid.canGoDown()){
            celdasAdyacentes.add(Mouse.DOWN);
        }
        //Caso3 Puede mover para la izquierda
        if (currentGrid.canGoLeft()){
            celdasAdyacentes.add(Mouse.LEFT);
        }
        //Caso4 Puede mover para la derecha
        if (currentGrid.canGoRight()){
            celdasAdyacentes.add(Mouse.RIGHT);
        }
        //Creamos una posicion random para empezar
        Random R = new Random();
        Integer r = R.nextInt(celdasAdyacentes.size());

        return celdasAdyacentes.get(r);
    }

    /**
     * @brief Método que se llama cuando la pila de movimientos no está vacia
     *
     */
    public int ultimoMov(Integer x, Integer y){
        int resultado = 0;
        //Observamos la casilla anterior
        Grid anterior = pilaMovimientos.pop();
        //Comparamos el movimiento anterior y vemos si va a la izquierda
        if (x-1 == anterior.getX()){
            //Si va hacia la izquierda se mueve hacia la izquierda
            resultado = Mouse.LEFT;
        }
        //Comparamos el movimiento anterior y vemos si va a la derecha
        if (x+1 == anterior.getX()){
            //Si va hacia la derecha se mueve hacia la derecha
            resultado =  Mouse.RIGHT;
        }
        //Comparamos el movimiento anterior y vemos si va hacia abajo
        if (y-1 == anterior.getY()){
            //Si va hacia la derecha se mueve hacia la abajo
            resultado =  Mouse.DOWN;
        }
        //Comparamos el movimiento anterior y vemos si va hacia arriba
        if (y+1 == anterior.getY()){
            //Si va hacia la derecha se mueve hacia la arriba
            resultado =  Mouse.UP;
        }
        return resultado;
    }

    /**
     * @brief Método que se llama cuando aparece un nuevo queso
     */
    @Override
    public void newCheese() {
        
    }

    /**
     * @brief Método que se llama cuando el raton pisa una bomba
     */
    @Override
    public void respawned() {
        
    }

    /**
     * @brief Método para evaluar que no nos movamos a la misma celda anterior
     * @param direction Direccion del raton
     * @param currentGrid Celda actual
     * @return True Si las casillas X e Y anterior son distintas a las actuales
     */
    public boolean testGrid(int direction, Grid currentGrid) {
        if (lastGrid == null) {
            return true;
        }
        
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        
        switch (direction) {
            case UP:
                y += 1;
                break;
            
            case DOWN:
                y -= 1;
                break;
            
            case LEFT:
                x -= 1;
                break;
            
            case RIGHT:
                x += 1;
                break;
        }
        
        return !(lastGrid.getX() == x && lastGrid.getY() == y);
        
    }

    /**
     *
     * @brief Método que devuelve si de una casilla dada, está contenida en el
     * mapa de celdasVisitadas
     * @param casilla Casilla que se pasa para saber si ha sido visitada
     * @param direccion Dirección de la casilla visitada
     * @return True Si la casilla vecina que indica la dirección había sido
     * visitada
     */
    public boolean visitada(Grid casilla, int direccion) {
        int x = casilla.getX();
        int y = casilla.getY();
        
        switch (direccion) {
            case UP:
                y += 1;
                break;
            
            case DOWN:
                y -= 1;
                break;
            
            case LEFT:
                x -= 1;
                break;
            
            case RIGHT:
                x += 1;
                break;
        }
        Pair par = new Pair(x, y);
        return celdasVisitadas.containsKey(par);
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es mayor que la de la anterior
     */
    public boolean actualArriba(Grid actual, Grid anterior) {
        return actual.getY() > anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es menor que la de la anterior
     */
    public boolean actualAbajo(Grid actual, Grid anterior) {
        return actual.getY() < anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es mayor que la de la anterior
     */
    public boolean actualDerecha(Grid actual, Grid anterior) {
        return actual.getX() > anterior.getX();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es menor que la de la anterior
     */
    public boolean actualIzquierda(Grid actual, Grid anterior) {
        return actual.getX() < anterior.getX();
    }

    /**
     *
     * @author josema
     * @param <U> First field (key) in a Pair
     * @param <V> Second field (value) in a Pair
     */
// Pair class
    class Pair<U, V> {

        public final U first;       // el primer campo de un par
        public final V second;      // el segundo campo de un par

        // Construye un nuevo par con valores especificados
        private Pair(U first, V second) {
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
    
} // class M23E04
