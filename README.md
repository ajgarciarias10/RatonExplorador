# RatonExplorador 🐭🔎

Práctica de la **Universidad de Jaén** realizada por **Antonio José García Arias** y **Manuel Cámara Serrano**.  
El proyecto implementa un sistema de **búsqueda por profundidad limitada** (Depth-Limited Search, DLS) para resolver un entorno tipo “ratón que explora” (laberinto/escenario). Desarrollado en **Java**.  

> Repo original: `ajgarciarias10/RatonExplorador`  
> Lenguaje principal: **Java** (100%)

---

## ✨ Objetivos de la práctica

- Implementar la estrategia **Depth-Limited Search**.
- Representar estados y transiciones de forma clara.
- Comparar el comportamiento del algoritmo con diferentes límites de profundidad.
- Practicar estructura y empaquetado de proyectos Java.

---

## 🧠 Algoritmo (resumen)

**Búsqueda por profundidad limitada (DLS)**: igual que DFS, pero acotando la profundidad máxima `L`.  
- Pros: bajo uso de memoria, explora en profundidad caminos prometedores.  
- Contras: puede **no encontrar** solución si la solución está a más profundidad que `L`, o tardar si se elige mal `L`.

> Idea básica:
> 1. Expandir sucesores del nodo actual hasta alcanzar `L`.  
> 2. Si se alcanza el objetivo, devolver la ruta.  
> 3. Si se llega al límite, **cortar** (backtrack) y continuar por otra rama.

---

## 🗂️ Estructura del proyecto

> Paquete principal: `mouserun/`  
> Editor recomendado: **IntelliJ IDEA** (también puedes usar NetBeans/Eclipse).

---

## ✅ Requisitos

- **Java 11+** (vale Java 8+, pero se recomienda 11 o superior).
- IntelliJ IDEA / NetBeans / Eclipse (opcional, pero recomendado).

---

## ▶️ Cómo ejecutar

### Opción A — IntelliJ IDEA
1. **Abrir proyecto**: `File > Open...` y selecciona la carpeta del repo.
2. Asegúrate de que el **SDK de proyecto** es Java 11+ (`File > Project Structure > Project SDK`).
3. Ubica la clase `main` en el paquete `mouserun` y ejecútala con **Run ▶**.

### Opción B — Línea de comandos
```bash
# Estando en la raíz del repo:
javac -d out $(find src -name "*.java")
java -cp out mouserun.Main   # Sustituye 'Main' por la clase con main(...)

 
 
 
