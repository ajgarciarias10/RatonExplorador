package mouserun.mouse;

import mouserun.functions.Pair;
import mouserun.game.*;
import java.util.*;
public class M23E04_bpl extends Mouse  {
    /**
     * Definimos la profundidad Maxima
     */
     private int profundidadMaxima =  0;
    /**
     * Vemos si se ha encontrado el queso
     */
      private boolean quesoEncontrado = false;
    /**
     * Lista de posibles movimientos
     */
    private ArrayList<Integer> posMovi;
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
     * Pila de nodos
     */
    private final Stack<Nodo> piladeNodos;


    public M23E04_bpl() {
        super("Ratonsito Perez");
        //Inicializamos las siguientes variables
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        posMovi = new ArrayList<>();
        piladeNodos = new Stack<Nodo>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese) {

        //Mientras no encuentre queso y la pila de nodos no este vacia
        //Buscamos el queso
        while (!quesoEncontrado && !piladeNodos.isEmpty()) {
            //Cogemos el nodo anterior
            Nodo nodoAnterior = piladeNodos.pop();

            //Comprobamos que ha pillado el queso
            if (compruebaQueso(nodoAnterior.casilla, cheese)) {
                quesoEncontrado = true;
                break;
            }
            //Si no lo pilla
            else{
                //Si el nodoAnterior la profundidad es mejor que la profundidad maxima
                //Signfica
                if (nodoAnterior.profundidad < profundidadMaxima) {
                    if (nodoAnterior.casilla == null) {
                        break;
                    } if(celdasVisitadas.containsKey(nodoAnterior.casilla)) {
                        break;
                    }else {
                        for (Grid childGrid : obtenerHijos(nodoAnterior)) {
                            Nodo nodohijo = new Nodo(childGrid, nodoAnterior, nodoAnterior.profundidad + 1);
                            piladeNodos.push(nodohijo);
                        }
                    }
                }
            }
            profundidadMaxima++;
        }
        return movimientodelratonsito(currentGrid);
    }


        /**
         *   Metodo utilizado para mover el raton utilizado anteriormente en los anteriores ejercicio
         */
    private int movimientodelratonsito(Grid currentGrid){
        //region Comprueba celda Visitada()
        compruebaCeldaVisit(currentGrid);
        //endregion
        posMovi.clear();
        posiblesMoves(currentGrid, posMovi);
        /**
         * 1º Comprobamos si esta en el inicio pila de Movimientos y
         * posibles movimientos estan vacios
         */
        if (posMovi.isEmpty()) {
            if (pilaMovimientos.isEmpty()) {
                return inicio(currentGrid);
            }
            //En el caso de que no significa que esta encerrado
            else {
                return ultimoMov(currentGrid.getX(), currentGrid.getY());
            }
        }
        /**
         * 2º Si la pila de movimientos y  de posibles movimientos
         *  no esta vacia
         */
        else {
            //Añadimos la Casilla actual  a la pila de movimientos
            pilaMovimientos.add(currentGrid);
            //Devolvemos el primero movimiento hecho
            return posMovi.get(0);

        }

    }
    List<Grid> obtenerHijos(Nodo node) {
        List<Grid> children = new ArrayList<>();
        try {
            if (node.casilla.canGoUp()){
                children.add(new Grid(node.casilla.getX(), node.casilla.getY()+1));
            }
            //Caso2 Puede mover para abajo
            if (node.casilla.canGoDown()){
                children.add(new Grid(node.casilla.getX(), node.casilla.getY()-1));
            }
            //Caso3 Puede mover para la izquierda
            if (node.casilla.canGoLeft()){
                children.add(new Grid(node.casilla.getX() -1 , node.casilla.getY()));
            }
            //Caso4 Puede mover para la derecha
            if (node.casilla.canGoRight()){
                children.add(new Grid(node.casilla.getX() +1, node.casilla.getY()));
            }

        }catch (Exception e){
            e.getMessage();
        }

        return children;
    }

    /**
     * ¿Se ha encontrado el queso?
     */
    private boolean compruebaQueso(Grid casillaActual,Cheese chesse){
        
        Grid casillaQuesos = new Grid(chesse.getX(), chesse.getY());

        if(casillaActual == casillaQuesos){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * @brief Método que comprueba la celda visitada
     *
     */
    private void compruebaCeldaVisit(Grid currentGrid) {

      Pair  posiCelda = new Pair<>(currentGrid.getX(),currentGrid.getY());

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
     *        Devuelve un movimiento cualquiera para comenzar
     *
     */
    private Integer inicio(Grid currentGrid) {
        //Inicializamos las Celdas Adyancentes
        ArrayList<Integer> celdasAdyacentes = new ArrayList<>();
        //Comprobamos los posibles casos de movimiento
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
        //En funcion de las posibilidades realiza un movimiento aleatorio
        Random R = new Random();
        Integer r = R.nextInt(celdasAdyacentes.size());
        //Agregamos el Nodo Padre
        Nodo nodoEntrada = new Nodo(currentGrid,null,0);
        piladeNodos.push(nodoEntrada);
        
        return celdasAdyacentes.get(r);
    }


    /**
     * @return
     * @brief Método que comprueba los posibles movimientos a partir de su posicion actual
     */
    private void posiblesMoves(Grid currentGrid , ArrayList<Integer> movimientos) {
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        //region Casos de posibles movimientos
        //Caso 1 (En caso de que vaya hacia arriba "y+1" )
        Pair celdaSuperior = new Pair(x, y + 1);
        //Comprobamos si la casilla actual puede ir hacia arriba
        //Y si no contiene la celda de arriba
        if (currentGrid.canGoUp() && !celdasVisitadas.containsKey(celdaSuperior)) {
            //Añadimos el movimiento hacia arriba
            movimientos.add(Mouse.UP);
        }
        //Caso 2 (En caso de que vaya hacia abajo "y-1" )
        Pair celdaInferior = new Pair(x, y - 1);
        //Comprobamos si la casilla actual puede ir hacia abajo
        //Y si no contiene la celda de abajo
        if (currentGrid.canGoDown() && !celdasVisitadas.containsKey(celdaInferior)) {
            //Añadimos el movimiento hacia a abajo
            movimientos.add(Mouse.DOWN);
        }
        //Caso 3 (En caso de que vaya hacia la derecha  "x+1" )
        Pair celdaDerecha = new Pair(x + 1, y);
        //Comprobamos si la casilla actual puede ir hacia derecha
        //Y si no contiene la celda de derecha
        if (currentGrid.canGoRight() && !celdasVisitadas.containsKey(celdaDerecha)) {
            //Añadimos el movimiento hacia la derecha
            movimientos.add(Mouse.RIGHT);
        }
        //Caso 4 (En caso de que vaya hacia la izquierda  "x-1" )
        Pair celdaIzquierda = new Pair(x - 1, y);
        //Comprobamos si la casilla actual puede ir hacia izquierda
        //Y si no contiene la celda de izquierda
        if (currentGrid.canGoLeft() && !celdasVisitadas.containsKey(celdaIzquierda)) {
            //Añadimos el movimiento hacia la izquierda
            movimientos.add(Mouse.LEFT);
        }
        //endregion
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

    @Override
    public void newCheese() {
     
    }

    @Override
    public void respawned() {

    }
    
}

/**Objeto nodo muy util para este ejercicio
 * 
 */
class Nodo {
    Grid casilla;
    Nodo padre;

    int profundidad;
    public Nodo(Grid casilla, Nodo padre, int profundidad) {
        this.casilla = casilla;
        this.padre = padre;
        this.profundidad= profundidad;
    }
}




