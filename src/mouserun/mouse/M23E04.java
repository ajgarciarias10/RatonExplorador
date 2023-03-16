package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import mouserun.functions.Pair;
import mouserun.game.*;

/**
 * Clase que contiene el esqueleto del raton base para las prácticas de
 * Inteligencia Artificial del curso 2020-21.
 *
 * @author Antonio José García Arias , Manuel Cámara Serrano
 */
public class M23E04 extends Mouse {
    /**
     * Posicion x,y
     */
    private int x,y;
    /**
     * Par almacenador de  posicion x e y
     */
    private Pair posiCelda;
    /**
     * Lista de posibles movimientos
     */
    private ArrayList<Integer> movimientos;


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
     * 
     * Metodo para el movimiento del raton
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        //region Almacenamiento de la posicion
            //Obtenemos la posicion x de la celda en la que se encuentra
             x = currentGrid.getX();
            //Obtenemos la posicion y de la celda en la que se encuentra
            y = currentGrid.getY();
            //Clase Pair le mandamos las posiciones obtenidas de la celda actual
            posiCelda = new Pair<>(x,y);
        //endregion
        //region Comprueba celda Visitada()
            compruebaCeldaVisit(posiCelda,currentGrid);
        //endregion
        //region Comprobador casillas adyacentes a  la casilla actual (Posibles Movimientos)
        //Inicializamos un Arraylist para almacenar los Posibles Movimientos
        movimientos = new ArrayList<>();
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
        celdasVisitadas.clear();
    }

} // class M23E04
