package mouserun.mouse;

import mouserun.functions.Pair;
import mouserun.game.*;
import java.util.*;


public class M23E04_bpl extends Mouse  {
    /**
     * Creates a new instance of Mouse.
     *
     * @param name The name of the Mouse to appear in the game interface.
     */

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
   // private final Stack<Grid> pilaDeNodos;



    public M23E04_bpl() {
        super("Hola?");
        //Inicializamos las siguientes variables
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        //pilaDeNodos = new Stack<Grid>();
        posMovi = new ArrayList<>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese)  {


        //region Comprueba celda Visitada()
            compruebaCeldaVisit(currentGrid);
        //endregion
        posMovi.clear();
        posiblesMoves(currentGrid,posMovi);
        /**
         * 1º Comprobamos si esta en el inicio pilMovimientos y posibles movimientos estan vacios
         */
        if(posMovi.isEmpty()) {
            if(pilaMovimientos.isEmpty()){

                return inicio(currentGrid);
            }
            //En el caso de que no significa que esta encerrado
            else{
                return  ultimoMov(currentGrid.getX(),currentGrid.getY());
            }
        }
        /**
         * 2º Si la pila  de posibles movimientos
         *  no esta vacia
         */
        else{

            //Añadimos la Casilla actual  a la pila de movimientos
            pilaMovimientos.add(currentGrid);
            //Devolvemos el primero movimiento hecho
            return posMovi.get(0);
        }


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

    @Override
    public void newCheese() {

    }

    @Override
    public void respawned() {

    }
}

