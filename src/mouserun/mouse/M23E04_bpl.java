package mouserun.mouse;

import mouserun.functions.Pair;
import mouserun.game.*;
import java.util.*;
public class M23E04_bpl extends Mouse  {

    private  boolean primerQueso = true ;


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
    private   ArrayList<Integer> posMovi = new ArrayList<>();;
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

    private Stack<Grid> camino = new Stack<>();

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
            return  explorar(currentGrid);
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
            return actToNext(currentGrid,camino.pop()); // Ejecutar busqueda
        }
    }

    @Override
    public void newCheese() {
            nuevoQueso = true;
            celdasCerradas.clear();

    }

    private Boolean busquedaProfundidadLimitada(Grid currentGrid, Cheese cheese,int profundidadMax) {
            insertaCerradas(currentGrid);
            camino.push(currentGrid);
            if(busquedaprofundidadBis(currentGrid,cheese,profundidadMax)){
                camino = revertirStack(camino);
                return  true;
            }
            else return false;
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
        ArrayList<Grid> posCasillas = obtenerHijos(currentGrid);
        for (Grid casillasHijo : posCasillas) {
            camino.push(casillasHijo);
            // MARCAR LA CELDA COMO VISITADA
            insertaCerradas(casillasHijo);
            //celdasVisitadas.put(new Pair<>(casillasHijo.getX(), casillasHijo.getY()),casillasHijo);


            if(busquedaprofundidadBis(casillasHijo,cheese,profundidad-1)){
                return true;
            } else {
                camino.pop();
            }
        }
        return false;  // no debería llegar aquí
    }


    private void insertaCerradas(Grid currentGrid) {
        celdasCerradas.put(new Pair<>(currentGrid.getX(), currentGrid.getY()),currentGrid);
    }


    private int explorar(Grid currentGrid){
                //Crear los posibles movimientos teniendo en cuenta visitadas y lo almacena en posmovi
                creaPosiblesMovimientos(currentGrid);
                /**
                 * Casos de donde puede estar el raton en funcion de su posicion
                 */
                return  devolvermovimiento(currentGrid);


    }

    private int devolvermovimiento(Grid currentGrid) {
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
            Grid casillaHaciaArriba = new Grid(casilla.getX(), casilla.getY()+1);
            if (casilla.canGoUp() && celdasVisitadas.containsKey(casillaHaciaArriba) && !celdasCerradas.containsKey(casillaHaciaArriba)){
                gridsHijos.add(celdasVisitadas.get(casillaHaciaArriba));
            }
            //Caso2 Puede mover para abajo
            Grid casillaHaciaAbajo = new Grid(casilla.getX(), casilla.getY()-1);
            if (casilla.canGoDown() && celdasVisitadas.containsKey(casillaHaciaAbajo) && !celdasCerradas.containsKey(casillaHaciaAbajo)){
                gridsHijos.add(celdasVisitadas.get(casillaHaciaAbajo));
            }
            //Caso3 Puede mover para la izquierda
            Grid casillaHaciaIzq = new Grid(casilla.getX() -1, casilla.getY());
            if (casilla.canGoLeft() && celdasVisitadas.containsKey(casillaHaciaIzq) && !celdasCerradas.containsKey(casillaHaciaIzq)){
                gridsHijos.add(celdasVisitadas.get(casillaHaciaIzq));
            }
            Grid casillaHaciaDerec = new Grid(casilla.getX() +1, casilla.getY());
            //Caso4 Puede mover para la derecha
            if (casilla.canGoRight() && celdasVisitadas.containsKey(casillaHaciaDerec) && !celdasCerradas.containsKey(casillaHaciaDerec)){
                gridsHijos.add(celdasVisitadas.get(casillaHaciaDerec));
            }


        }catch (Exception e){
            e.getMessage();
        }
        return gridsHijos;

    }


    /**
     * @return
     * @brief Método que comprueba los posibles movimientos a partir de su posicion actual
     */
    private void creaPosiblesMovimientos(Grid currentGrid) {
            //Cada ejecucion la lista de posibles movimientos la limpiamos
            posMovi.clear();
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
        else if (actual.getX() +1 == next.getX()){
            resultado =  Mouse.RIGHT;
        }
        //Comparamos el movimiento anterior y vemos si va hacia abajo
        else if (actual.getY() -1 == next.getY()){
            resultado =  Mouse.DOWN;
        }
        //Comparamos el movimiento anterior y vemos si va hacia arriba
        else if (actual.getY() +1 == next.getY()){
            resultado =  Mouse.UP;
        }

        
        
        return resultado;
    }



    @Override
    public void respawned() {

    }
    
}






