package mouserun.mouse;
import mouserun.game.Cheese;
import mouserun.game.Grid;
import mouserun.game.Mouse;

import java.util.*;

public class M23E04_extra extends Mouse  {

    /**
     * Definimos la profundidad Maxima
     */
    private static final int MAX_PROFUNDIDAD = 300;
    /**
     * Nuevo queso util para cuando se cambie el queso
     */
    private   boolean nuevoQueso;

    /**
     * Tabla hash para almacenar las celdas visitadas por el raton:
     * Clave:Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    /**
     * Tabla hash  en la que el raton aprende esas celdas:
     * Clave:Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasAprendidas;

    /**
     * Pila que contiene todos los movimientos del ratón, util para que cuando se encierre sepa desandar lo recorrido
     */
    private final Stack<Grid> pilaMovimientos ;

    /**
     * Pila con camino a recorrer después de realizar una busqueda
     */
    private Stack<Grid> caminoBusqueda ;
    /**
     * HashMap de Celdas Cerradas durante la búsqueda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasCerradasEnBusqueda;
    private boolean busquedaProfundidadFallida = false;
    private int pasos;

    /**
     * Constructor
     */
    public M23E04_extra() {
        super("ELMillor");
        celdasVisitadas = new HashMap<>();
        celdasAprendidas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        caminoBusqueda = new Stack<>();
        celdasCerradasEnBusqueda = new HashMap<>();
        pasos = 0;
    }

    /**
     *  Método implementado del raton
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {

        /**-----------------------------------------------------------
         * Metemos en la pila de movimientos la casilla actual
         * -----------------------------------------------------------
         * Y insertamos las celdas Aprendidas en la casilla actual
         *
         */
        pilaMovimientos.push(currentGrid);
        insertaEnCeldasAprendidas(currentGrid);

        // Ponemos bombas cada 20 movimientos si se cumplen las condiciones
            if (pasos > 20 ) {
                pasos = 0;
                return Mouse.BOMB;
            } else {
                pasos++;
            }


        // ¿Posicion del queso conocida?
        if(!celdasAprendidas.containsKey(new Pair<>(cheese.getX(),cheese.getY()))){
            insertaCeldaVisitada(currentGrid);
            return explorar(currentGrid,true);
        }
        else {
            //region Calculo Recursivo
            if(nuevoQueso || busquedaProfundidadFallida){
                nuevoQueso = false;

                if (busquedaProfundidadFallida = !busquedaProfundidadLimitada(currentGrid,cheese, MAX_PROFUNDIDAD))
                    caminoBusqueda.clear();
            }
            //endregion
            if (!caminoBusqueda.isEmpty())
                return dameMovimiento(currentGrid, caminoBusqueda.pop());
            else {
                return explorar(currentGrid,false);
            }

        }
        //endregion
    }
    /**-----------------------------------------------------------
     * Insertamos en celdas aprendidas las cosas
     *
     */
    private void insertaEnCeldasAprendidas(Grid currentGrid) {
        if (!celdasAprendidas.containsKey(newPair(currentGrid))) {
            celdasAprendidas.put(newPair(currentGrid), currentGrid);
            incExploredGrids();
        }
    }


    private Pair newPair(Grid currentGrid) {
        return new Pair<>(currentGrid.getX(),currentGrid.getY());
    }
    /**
     *  Método que comprueba la celda visitada
     */

    private boolean isCeldaVisitada(Grid currentGrid) {
        return celdasVisitadas.containsKey(newPair(currentGrid));
    }
    /**
     * @brief Primer método utilizado insertar celda visitada
     *
     */
    private void insertaCeldaVisitada(Grid currentGrid) {
        if(!isCeldaVisitada(currentGrid)){
            //Lo metemos en el HashMap  celdas vistadas
            celdasVisitadas.put(newPair(currentGrid),currentGrid);
            //LLamamos al metodo de clase que hace que incremente el numero de celdas visitadas
            //Para asi tener un conteo de ellas
        }
    }

    /**
     * Metodo de Busqueda de profundidad limitada
     * @param currentGrid
     * @param cheese
     * @param profundidadMax
     * @return
     */
    private Boolean busquedaProfundidadLimitada(Grid currentGrid, Cheese cheese,int profundidadMax) {
        // cuando lanzo la busqueda inicializo la pila y la lista de cerradas
        caminoBusqueda.clear();
        celdasCerradasEnBusqueda.clear();
        //Cerrar nodo
        insertarCerradasEnBusqueda(currentGrid);
        if(busquedaprofundidadBis(currentGrid,cheese,profundidadMax)){
            caminoBusqueda = revertirStack(caminoBusqueda);
            return  true;
        }
        else {
            return false;
        }
    }

    /**
     * Metodo de Busqueda recursiva en el que obtenemos los hijos
     * @param currentGrid
     * @param cheese
     * @param profundidad
     */
    private boolean busquedaprofundidadBis(Grid currentGrid,Cheese cheese,int profundidad) {
        // ¿Está el queso aquí?
        if (compruebaQueso(currentGrid,cheese)) {
            return true;
        }

        if (profundidad == 0) {
            // HEMOS PASADO LA PROFUNDIDAD
            return false;
        }
        List<Grid> casillasHijos = obtenerHijos(currentGrid);
        for (Grid casillaHijo : casillasHijos) {
            caminoBusqueda.push(casillaHijo);
            // MARCAR LA CELDA COMO VISITADA
            insertarCerradasEnBusqueda(casillaHijo);
            if(busquedaprofundidadBis(casillaHijo,cheese,profundidad-1))
                return true;
            else caminoBusqueda.pop();
        }
        return false;
    }


    /**
     * ¿Se ha encontrado el queso en la casilla actual?
     */
    private boolean compruebaQueso(Grid casillaActual,Cheese chesse){
        return (casillaActual.getX() == chesse.getX() && casillaActual.getY() == chesse.getY());
    }


    /**
     *   Metodo utilizado para  devolver los posibles nodos hijos como un arraylist de casillas
     *
     */
    public List<Grid> obtenerHijos(Grid casilla) {
        List<Grid> gridsHijos = new ArrayList<>();
            Pair keyHaciaArriba= new Pair<>(casilla.getX(),casilla.getY()+1);
            if (casilla.canGoUp() && celdasAprendidas.containsKey(keyHaciaArriba) && !celdasCerradasEnBusqueda.containsKey(keyHaciaArriba)){
                gridsHijos.add(celdasAprendidas.get(keyHaciaArriba));
            }
            //Caso2 Puede mover para abajo
            Pair keyHaciaAbajo= new Pair<>(casilla.getX(),casilla.getY()-1);
            if (casilla.canGoDown() && celdasAprendidas.containsKey(keyHaciaAbajo) && !celdasCerradasEnBusqueda.containsKey(keyHaciaAbajo)){
                gridsHijos.add(celdasAprendidas.get(keyHaciaAbajo));
            }
            //Caso3 Puede mover para la izquierda
            Pair keyHaciaIzq= new Pair<>(casilla.getX()-1,casilla.getY());
            if (casilla.canGoLeft() && celdasAprendidas.containsKey(keyHaciaIzq) && !celdasCerradasEnBusqueda.containsKey(keyHaciaIzq)){
                gridsHijos.add(celdasAprendidas.get(keyHaciaIzq));
            }
            Pair keyHaciaDerec= new Pair<>(casilla.getX()+1,casilla.getY());
            //Caso4 Puede mover para la derecha
            if (casilla.canGoRight() && celdasAprendidas.containsKey(keyHaciaDerec) && !celdasCerradasEnBusqueda.containsKey(keyHaciaDerec)){
                gridsHijos.add(celdasAprendidas.get(keyHaciaDerec));
            }


        return gridsHijos;

    }


    /**
     * Metodo de inserccion de celdas cerradas durante la busqueda
     */

    private void insertarCerradasEnBusqueda(Grid currentGrid) {
        celdasCerradasEnBusqueda.put(new Pair<>(currentGrid.getX(), currentGrid.getY()),currentGrid);
    }

    /**
     * Metodo para darle la vuelta al camino
     * @param  stack
     */
    public static Stack<Grid> revertirStack(Stack<Grid> stack) {

        Stack<Grid> stackRevertido = new Stack<>();
        while (!stack.isEmpty()) {
            stackRevertido.push(stack.pop());
        }

        return stackRevertido;

    }



    /**
     * @brief Método que nos indica el movimiento del ratón para ir a celda @param next desde celda @param actual
     */

    public int dameMovimiento(Grid actual, Grid next){
        int resultado = 0;

        //Comparamos el movimiento anterior y vemos si va a la izquierda
        if (actual.getX()-1 == next.getX()){
            resultado = Mouse.LEFT;
        }
        //Comparamos el movimiento anterior y vemos si va a la derecha
        else if (actual.getX()+1 == next.getX()){
            resultado =  Mouse.RIGHT;
        }
        //Comparamos el movimiento anterior y vemos si va hacia abajo
        else if (actual.getY()-1 == next.getY()){
            resultado =  Mouse.DOWN;
        }
        //Comparamos el movimiento anterior y vemos si va hacia arriba
        else if (actual.getY()+1 == next.getY()){
            resultado =  Mouse.UP;
        }

        return resultado;
    }
    public Pair<Integer, Integer> damePair(Grid actual,int movimiento){
        switch (movimiento){
            case UP: return new Pair<>(actual.getX(),actual.getY()+1);
            case DOWN: return new Pair<>(actual.getX(),actual.getY()-1);
            case LEFT: return new Pair<>(actual.getX()-1,actual.getY());
            case RIGHT: return new Pair<>(actual.getX()+1,actual.getY());
            default: {
                return new Pair<>(actual.getX(),actual.getY());
            }

        }
    }


    /**
     * @brief Metodo de exploracion del raton
     *
     */
    private int explorar(Grid currentGrid,boolean mirarCeldaVisitada){
        //pilaMovimientos.push(currentGrid);
        //Crear los posibles movimientos teniendo en cuenta visitadas y lo almacena en posmovi
        List<Integer> listaMovimientos = creaPosiblesMovimientos(currentGrid, mirarCeldaVisitada);
        /**
         * Casos de donde puede estar el raton en funcion de su posicion
         */
        return devolvermovimiento(currentGrid,listaMovimientos,mirarCeldaVisitada);
    }

    /**
     * @brief Metodo para devolver el momiento del raton durante la exploracion
     *
     * Casos:
     * Caso 1: Inicio  ¿Esta la pila de movimientos vacia?
     * Caso 2: Encerrado ¿Hay algun movimiento posible?-> Caso de que no
     * Caso 3: Movimiento normal ¿Hay algun movimiento posible? -> Caso de que Si
     *
     */
    private int devolvermovimiento(Grid currentGrid, List<Integer> listaMovimientos, boolean mirarCeldaVisitada){
        if (listaMovimientos.isEmpty()) { // si ocurre esto es porque nos hemos encerrado
            return retrocede(currentGrid);
        } else {
            if (mirarCeldaVisitada)
                return listaMovimientos.get(0);
            else {
                return movimientoPorBusquedaFallida(currentGrid, listaMovimientos);
            }
        }
    }

    private int movimientoPorBusquedaFallida(Grid currentGrid, List<Integer> listaMovimientos) {
        // estamos en el caso de querer movernos cuando ha fallado la búsqueda y queremos ir al cualquier casilla donde cumpla:
        // 1. que sea hija de la actual: todas las hijo de la currentGrid ya están en listaMovimientos
        // 2. que sea una celdaAprendida, pues ya tuvo que estar antes en ella, porque sino, la búsqueda no se habría lanzado
        // 3. y que no sea de donde ha venido, porque si vuelvo ahí estamos retrocediendo y no avanzando
        pilaMovimientos.pop(); // sacamos el actual para conocer el anterior
        int movParaIrALaAnterior= dameMovimiento(currentGrid,pilaMovimientos.peek());
        pilaMovimientos.push(currentGrid); // volvemos a dejar bien donde estamos
        Collections.shuffle(listaMovimientos); // podemos meter un movimiento aleatorio por si hay más de uno valido
        for (int movimiento: listaMovimientos){
            if (movimiento!=movParaIrALaAnterior && celdasAprendidas.containsKey(damePair(currentGrid,movimiento)))
                return movimiento;
        }
        // si se ejecuta esta linea mal vamos
        return listaMovimientos.get((int) (Math.random() * listaMovimientos.size()));
    }



    private int retrocede(Grid currentGrid) {
        pilaMovimientos.pop();
        return dameMovimiento(currentGrid, pilaMovimientos.pop());
    }


    /**
     * @return
     * @brief Método que comprueba los posibles movimientos a partir de su posicion actual
     *
     */
    private List<Integer>  creaPosiblesMovimientos(Grid currentGrid, boolean mirarCeldaVisitada) {
        //Cada ejecucion la lista de posibles movimientos la limpiamos
        List<Integer>  posMovi= new ArrayList<>();
        //region Casos de posibles movimientos
        //Caso 1 (En caso de que vaya hacia arriba "y+1" )
        //Comprobamos si la casilla actual puede ir hacia arriba
        //Y si no contiene la celda de arriba
        if (currentGrid.canGoUp() && (!mirarCeldaVisitada || !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()+1 )))) {
            //Añadimos el movimiento hacia arriba
            posMovi.add(Mouse.UP);
        }
        //Caso 2 (En caso de que vaya hacia abajo "y-1" )
        //Comprobamos si la casilla actual puede ir hacia abajo
        //Y si no contiene la celda de abajo
        if (currentGrid.canGoDown() && (!mirarCeldaVisitada || !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()-1)))) {
            //Añadimos el movimiento hacia a abajo
            posMovi.add(Mouse.DOWN);
        }
        //Caso 3 (En caso de que vaya hacia la derecha  "x+1" )
        //Comprobamos si la casilla actual puede ir hacia derecha
        //Y si no contiene la celda de derecha
        if (currentGrid.canGoRight() && (!mirarCeldaVisitada || !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX()+1, currentGrid.getY())))) {
            //Añadimos el movimiento hacia la derecha
            posMovi.add(Mouse.RIGHT);
        }
        //Caso 4 (En caso de que vaya hacia la izquierda  "x-1" )
        //Comprobamos si la casilla actual puede ir hacia izquierda
        //Y si no contiene la celda de izquierda
        if (currentGrid.canGoLeft() && (!mirarCeldaVisitada || !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX()-1, currentGrid.getY())))) {
            //Añadimos el movimiento hacia la izquierda
            posMovi.add(Mouse.LEFT);
        }
        //endregion
        return posMovi;
    }


    /**
     * Metodo implementados de desde la clase raton
     */
    @Override
    public void newCheese() {
        nuevoQueso = true;
        // lobotomizando
        celdasVisitadas.clear();
    }
    @Override
    public void respawned() {
        caminoBusqueda.clear();
        celdasVisitadas.clear();
        pilaMovimientos.clear();
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

}