
  
  
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software – ARSW


## Laboratorio – Programación concurrente, condiciones de carrera y sincronización de hilos - Caso Inmortales

### Descripción
Este laboratorio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente, además de estrategias que eviten condiciones de carrera.
### Dependencias:

* [Ejercicio Introducción al paralelismo - Hilos - BlackList Search](https://github.com/ARSW-ECI-beta/PARALLELISM-JAVA_THREADS-INTRODUCTION_BLACKLISTSEARCH)
#### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

1. Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?

	Este es el consumo en el PC que lo ejecutamos: 

	![image](https://user-images.githubusercontent.com/50029247/106894929-9a638e80-66bd-11eb-8507-af797af3c8ab.png)

	Este consumo se debe a las clases Consumer.java y Producer.Java, esta última inicia con 5 segundos de stock pero en su ejecución  espera 1 segundo antes de seguir añadiendo datos a la cola, en cambio, Consumer.java se inicia 5 segundos despues de Producer.Java pero como no espera antes del ultimo poll() hecho a la cola se sincronizan casi de inmediato, es decir, cuando Producer.Java ingresa un valor Consumer.Java ya lo esta sacando de la cola con el poll().

2. Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.

	Agregamos un hilo produtor para asi intentar "igualar" la velocidad en que se produce y la que se consume. En la siguiente imagen podemos confirmar que no se llega ni al 50% de uso de la CPU como en la anterior imagen.

	![image](https://user-images.githubusercontent.com/50029247/106901809-27aae100-66c6-11eb-966f-2280ed204e97.png)

3. Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.

	Con el metodo Thread.sleep() hicimos que el consumidor esperara después de sacar un elemento de la cola. Asi el productor es más rapido que el consumidor ya que el empieza 5 segundos antes que el consumidor.

	![image](https://user-images.githubusercontent.com/50029247/106903542-1a8ef180-66c8-11eb-8b93-b8d691f85ebc.png)

	Con este condicional hacemos que se respete el stock limite que tiene el productor y si la cola es igual a este limite no agrega mas elementos.

	![image](https://user-images.githubusercontent.com/50029247/106903727-588c1580-66c8-11eb-86da-cc40aafa2ad8.png)

	Con un stock limite "pequeño" (100) el programa tiene consumo de CPU casi nulo y no tiene errores. 

	![image](https://user-images.githubusercontent.com/50029247/106903856-8709f080-66c8-11eb-8632-75499e053c2e.png)

	![image](https://user-images.githubusercontent.com/50029247/106903400-ee737080-66c7-11eb-97ed-092c9868e1fb.png)

#### Parte II. – Antes de terminar la clase.

Teniendo en cuenta los conceptos vistos de condición de carrera y sincronización, haga una nueva versión -más eficiente- del ejercicio anterior (el buscador de listas negras). En la versión actual, cada hilo se encarga de revisar el host en la totalidad del subconjunto de servidores que le corresponde, de manera que en conjunto se están explorando la totalidad de servidores. Teniendo esto en cuenta, haga que:

- La búsqueda distribuida se detenga (deje de buscar en las listas negras restantes) y retorne la respuesta apenas, en su conjunto, los hilos hayan detectado el número de ocurrencias requerido que determina si un host es confiable o no (_BLACK_LIST_ALARM_COUNT_).
	
	Al crear una bandera en el validador que significa: si algun hilo ya encontro las ocurrencias necesarias para reportar un HOST. Y hacer que cada hilo antes de seguir buscando revise esta bandera, si la bandera el true detenemos el hilo  cuando ya que se han encontrado el numero de ocurrencias necesarias para reportar el HOST en otro hilo.
	
	![image](https://user-images.githubusercontent.com/50029247/106910299-15817080-66cf-11eb-8152-ea06d8ff9cf8.png)


- Lo anterior, garantizando que no se den condiciones de carrera.

	En este caso no hay ninguna condicion de carrera ya que cada hilo se detiene cuando el numero de ocurrencias encontradas es mayor o igual al numero especificado para reportar un HOS o cuando otro HILO halla encontrado las ocurrencias este cambia la bandera a TRUE y los demas hilos que se dan cuenta se detienen también. Si un hilo no cumple esta condición se ejecuta completamente.

#### Parte II. – Avance para la siguiente clase

Sincronización y Dead-Locks.

![](http://files.explosm.net/comics/Matt/Bummed-forever.png)

1. Revise el programa “highlander-simulator”, dispuesto en el paquete edu.eci.arsw.highlandersim. Este es un juego en el que:

	* Se tienen N jugadores inmortales.
	* Cada jugador conoce a los N-1 jugador restantes.
	* Cada jugador, permanentemente, ataca a algún otro inmortal. El que primero ataca le resta M puntos de vida a su contrincante, y aumenta en esta misma cantidad sus propios puntos de vida.
	* El juego podría nunca tener un único ganador. Lo más probable es que al final sólo queden dos, peleando indefinidamente quitando y sumando puntos de vida.
	
![image](https://user-images.githubusercontent.com/50029247/107594876-1d667680-6be1-11eb-86d9-55bc6f24895a.png)

Se evidencia que al ejecutar el programa "highlander-simulator" nunca termina ya que siempre se estan atacando los inmortales y se evidencia siempre cambio en la suma de sus vidas, pero como un bucle infinito.

2. Revise el código e identifique cómo se implemento la funcionalidad antes indicada. Dada la intención del juego, un invariante debería ser que la sumatoria de los puntos de vida de todos los jugadores siempre sea el mismo(claro está, en un instante de tiempo en el que no esté en proceso una operación de incremento/reducción de tiempo). Para este caso, para N jugadores, cual debería ser este valor?.

![image](https://user-images.githubusercontent.com/50029247/107595407-81d60580-6be2-11eb-9b91-4a2c3359410a.png)

Se evidencia en el codigo que la vida por defecto que se le asigna a cada inmortal es de 100 puntos por ende, la vidad todad de los inmortales es N*100 (N es esl numero de inmortales en juego).

3. Ejecute la aplicación y verifique cómo funcionan las opción ‘pause and check’. Se cumple el invariante?.

Después de iniciar la aplicación con el boton Start el juego empieza. Cuando se oprime el boton pause and check es juego muestra este resumen en el panel:

![image](https://user-images.githubusercontent.com/50029247/107595499-ccf01880-6be2-11eb-9e92-3bb63fdd9859.png)

Se evidencia que no se cumple la invariante y es por que no se garantiza detener el juego cuando se hallan completado todas las operaciones. Ya que la suma de los inmortales en este caso deberia ser 300 puntos de vida y muestra solo 280. 

4. Una primera hipótesis para que se presente la condición de carrera para dicha función (pause and check), es que el programa consulta la lista cuyos valores va a imprimir, a la vez que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que efectivamente, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Adicionalmente, implemente la opción ‘resume’.

5. Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). Se cumple o no el invariante?.

6. Identifique posibles regiones críticas en lo que respecta a la pelea de los inmortales. Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:

	```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
	}
	```

7. Tras implementar su estrategia, ponga a correr su programa, y ponga atención a si éste se llega a detener. Si es así, use los programas jps y jstack para identificar por qué el programa se detuvo.

8. Plantee una estrategia para corregir el problema antes identificado (puede revisar de nuevo las páginas 206 y 207 de _Java Concurrency in Practice_).

9. Una vez corregido el problema, rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante, debe analizar lo realizado en el paso 4.

10. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'inmortales' vivos realizando peleas fallidas con 'inmortales' ya muertos. Es necesario ir suprimiendo los inmortales muertos de la simulación a medida que van muriendo. Para esto:
	* Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'inmortales' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
	* Corrija el problema anterior __SIN hacer uso de sincronización__, pues volver secuencial el acceso a la lista compartida de inmortales haría extremadamente lenta la simulación.

11. Para finalizar, implemente la opción STOP.


<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br />Este contenido hace parte del curso Arquitecturas de Software del programa de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería, y está licenciado como <a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">Creative Commons Attribution-NonCommercial 4.0 International License</a>.
