package mouserun.mouse;

import mouserun.functions.Pair;
import mouserun.game.*;
import java.util.*;
public class M23E04_bpl extends Mouse  {

    /**
     * HashMap de Celdas Cerradas
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasCerradas = new HashMap<>();
    /**
     * Definimos la profundidad Maxima
     */
    private static final int MAX_PROFUNDIDAD = 4;
    /**
     * Nuevo queso util para cuando se cambie el queso
     */
    private   boolean nuevoQueso;

    /**
     * Lista de posibles movimientos
     */

   /**
            * Tabla hash para almacenar las celdas visitadas por el raton:
            * Clave:Coordenadas Valor: La celda
    */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas = new HashMap<>();

    /**
     * Pila para almacenar el camino recorrido.
     */
    private final Stack<Grid> pilaMovimientos = new Stack<>();

    /**
     * Pila de nodos camino
     */

    private Stack<Grid> caminoBusqueda = new Stack<>();

    public M23E04_bpl() {
        super("Ratón Vacilon");
    }


    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        //region Comprueba con el metodo isCeldaVisit y la marca
        insertaCeldaVisitada(currentGrid);
        //endregion
        //¿Posicion del queso conocida?
        if(!celdasVisitadas.containsKey(new Pair<>(cheese.getX(),cheese.getY()))){
            return explorar(currentGrid);
        }
        else {
            //Recalcular
            if(nuevoQueso){
                //camino.clear();
                nuevoQueso = false;
                //Calcular busqueda
                int multiplicador=1;
                while(!busquedaProfundidadLimitada(currentGrid,cheese, MAX_PROFUNDIDAD*multiplicador)){
                    multiplicador++;
                }
            }
            return actToNext(currentGrid, caminoBusqueda.pop()); // Ejecutar busqueda
        }
    }

    @Override
    public void newCheese() {
        nuevoQueso = true;

        celdasCerradas.clear();
        caminoBusqueda.clear();
        // ñññ Cuando aparece un nuevo queso si este no estuviera en las celdas visitadas  tendriamos que volver a explorar
        // ¿sería necesario reiniciar la pila de movimientos de la exploración? Parece que no que
        //movimientosEnExploracion.clear();

    }

    private Boolean busquedaProfundidadLimitada(Grid currentGrid, Cheese cheese,int profundidadMax) {
        caminoBusqueda.push(currentGrid);
        insertaCerradas(currentGrid);
        if(busquedaprofundidadBis(currentGrid,cheese,profundidadMax)){
            caminoBusqueda = revertirStack(caminoBusqueda);
            return  true;
        }
        else {
            System.err.printf("WARNING: Profundidad %d - ¿BUCLE INFINITO?\n",profundidadMax);
            return false;
        }
    }

    public static Stack<Grid> revertirStack(Stack<Grid> stack) {

        Stack<Grid> stackRevertido = new Stack<>();
        while (!stack.isEmpty()) {
            stackRevertido.push(stack.pop());
        }

        return stackRevertido;

    }


    private boolean busquedaprofundidadBis(Grid currentGrid,Cheese cheese,int profundidad) {
        // ¿Está el queso aquí?
        if (compruebaQueso(currentGrid,cheese)) {
            return true;
        }

        if (profundidad == 0) {
            // HEMOS PASADO LA PROFUNDIDAD
            return false;
        }
        ArrayList<Grid> casillasHijos = obtenerHijos(currentGrid);
        for (Grid casillaHijo : casillasHijos) {
            caminoBusqueda.push(casillaHijo);
            // MARCAR LA CELDA COMO VISITADA
            insertaCerradas(casillaHijo);
            if(busquedaprofundidadBis(casillaHijo,cheese,profundidad-1))
                return true;
            else caminoBusqueda.pop();
        }
        System.out.printf("Profundidad %d - Celda actual (%d,%d) - pila actual (%d,%d)\n",profundidad,currentGrid.getX(),currentGrid.getY(),caminoBusqueda.peek().getX(),caminoBusqueda.peek().getY());
        return !caminoBusqueda.isEmpty();
    }


    private void insertaCerradas(Grid currentGrid) {
        celdasCerradas.put(new Pair<>(currentGrid.getX(), currentGrid.getY()),currentGrid);
    }


    private int explorar(Grid currentGrid){

        //Crear los posibles movimientos teniendo en cuenta visitadas y lo almacena en posmovi
        List<Integer> posMovi = creaPosiblesMovimientos(currentGrid);
        /**
         * Casos de donde puede estar el raton en funcion de su posicion
         */
        return devolvermovimiento(currentGrid,posMovi);
    }

    private int devolvermovimiento(Grid currentGrid,List<Integer> posMovi){
        //Inicio
        if(pilaMovimientos.isEmpty()){
            pilaMovimientos.push(currentGrid);
            //En funcion de las posibilidades realiza un movimiento aleatorio
            return posMovi.get(new Random().nextInt(posMovi.size()));
            //return inicio(currentGrid);
        }
        //Se esta moviendo o esta moviAnteriorPila
        else{
            if(posMovi.isEmpty())
                return actToNext(currentGrid, pilaMovimientos.pop());
            //Se esta moviendo
            else{
                //Añadimos la Casilla actual  a la pila de movimientos
                pilaMovimientos.push(currentGrid);
                //Devolvemos el primero movimiento hecho
                return posMovi.get(0);
            }
        }
    }

    /**
     * ¿Se ha encontrado el queso?
     */
    private boolean compruebaQueso(Grid casillaActual,Cheese chesse){
        return (casillaActual.getX() == chesse.getX() && casillaActual.getY() == chesse.getY());
    }


    private boolean isCeldaVisitada(Grid currentGrid) {
        return celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(),currentGrid.getY()));
    }
    /**
     * @brief Método que comprueba la celda visitada
     *
     */
    private void insertaCeldaVisitada(Grid currentGrid) {
        if(!isCeldaVisitada(currentGrid)){
            Pair celdaVisitada= new Pair<>(currentGrid.getX(),currentGrid.getY());
            //Lo metemos en el HashMap  celdas vistadas
            celdasVisitadas.put(celdaVisitada,currentGrid);
            //LLamamos al metodo de clase que hace que incremente el numero de celdas visitadas
            //Para asi tener un conteo de ellas
            this.incExploredGrids();
        }
    }
    /**
     *   Metodo utilizado para  devolver los posibles nodos hijos como un arraylist de casillas
     *
     */
    ArrayList<Grid> obtenerHijos(Grid casilla) {
        ArrayList<Grid> gridsHijos = new ArrayList<>();
        try {
            Pair keyHaciaArriba= new Pair<>(casilla.getX(),casilla.getY()+1);
            if (casilla.canGoUp() && celdasVisitadas.containsKey(keyHaciaArriba) && !celdasCerradas.containsKey(keyHaciaArriba)){
                gridsHijos.add(celdasVisitadas.get(keyHaciaArriba));
            }
            //Caso2 Puede mover para abajo
            Pair keyHaciaAbajo= new Pair<>(casilla.getX(),casilla.getY()-1);
            if (casilla.canGoDown() && celdasVisitadas.containsKey(keyHaciaAbajo) && !celdasCerradas.containsKey(keyHaciaAbajo)){
                gridsHijos.add(celdasVisitadas.get(keyHaciaAbajo));
            }
            //Caso3 Puede mover para la izquierda
            Pair keyHaciaIzq= new Pair<>(casilla.getX()-1,casilla.getY());
            if (casilla.canGoLeft() && celdasVisitadas.containsKey(keyHaciaIzq) && !celdasCerradas.containsKey(keyHaciaIzq)){
                gridsHijos.add(celdasVisitadas.get(keyHaciaIzq));
            }
            Pair keyHaciaDerec= new Pair<>(casilla.getX()+1,casilla.getY());
            //Caso4 Puede mover para la derecha
            if (casilla.canGoRight() && celdasVisitadas.containsKey(keyHaciaDerec) && !celdasCerradas.containsKey(keyHaciaDerec)){
                gridsHijos.add(celdasVisitadas.get(keyHaciaDerec));
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        if (gridsHijos.size()==0)
            System.err.printf("ERROR SIN HIJOS para celda %s\n",casilla);
        return gridsHijos;

    }


    /**
     * @return
     * @brief Método que comprueba los posibles movimientos a partir de su posicion actual
     */
    private List<Integer>  creaPosiblesMovimientos(Grid currentGrid) {
        //Cada ejecucion la lista de posibles movimientos la limpiamos
        List<Integer>  posMovi= new ArrayList<>();
        //region Casos de posibles movimientos
        //Caso 1 (En caso de que vaya hacia arriba "y+1" )
        //Comprobamos si la casilla actual puede ir hacia arriba
        //Y si no contiene la celda de arriba
        if (currentGrid.canGoUp() && !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1))) {
            //Añadimos el movimiento hacia arriba
            posMovi.add(Mouse.UP);
        }
        //Caso 2 (En caso de que vaya hacia abajo "y-1" )
        //Comprobamos si la casilla actual puede ir hacia abajo
        //Y si no contiene la celda de abajo
        if (currentGrid.canGoDown() && !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1))) {
            //Añadimos el movimiento hacia a abajo
            posMovi.add(Mouse.DOWN);
        }
        //Caso 3 (En caso de que vaya hacia la derecha  "x+1" )
        //Comprobamos si la casilla actual puede ir hacia derecha
        //Y si no contiene la celda de derecha
        if (currentGrid.canGoRight() && !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()))) {
            //Añadimos el movimiento hacia la derecha
            posMovi.add(Mouse.RIGHT);
        }
        //Caso 4 (En caso de que vaya hacia la izquierda  "x-1" )
        //Comprobamos si la casilla actual puede ir hacia izquierda
        //Y si no contiene la celda de izquierda
        if (currentGrid.canGoLeft() && !celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()))) {
            //Añadimos el movimiento hacia la izquierda
            posMovi.add(Mouse.LEFT);
        }
        //endregion
        return posMovi;
    }

    /**
     * @brief Método que se llama cuando la pila de movimientos no está vacia
     *
     */

    public int actToNext(Grid actual, Grid next){
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
        if (resultado == 0)
            System.err.printf("ERROR: Sin movimiento en Celda Actual(%d,%d) - Celda Sig(%d,%d)\n",actual.getX(),actual.getY(),next.getX(),next.getY());
        return resultado;
    }



    @Override
    public void respawned() {

    }
    
}






