# Parcial Magneto

## Introduccion
Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Mens.

Te ha contratado a ti para que desarrolles un proyecto que detecte si un humano es mutante basándose en su secuencia de ADN.

Para eso te ha pedido crear un programa con un método o función con la siguiente firma:

**isMutant(String[] dna)**

## Funcionamiento

Se recibirá como parámetro un array de Strings que representan cada fila de una tabla de (6x6) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las cuales representa cada base nitrogenada del ADN.

Se sabrá si un humano es mutante, si se encuentra **MAS DE UNA SECUENCIA** de cuatro letras iguales, de forma oblicua, horizontal o vertical.

Las filas de la matriz a verificar se ingresan por teclado.

Ejemplo de input: '**ATCGTA**' (esto equivale a una fila de la matriz)

Una vez cargada correctamente la misma, se aplica una función que verifica si hay presencia en la matriz de mutantes o no y se devuelve el resultado al usuario en base a eso.

## Ejecución

El proyecto ha sido deployado a Render y puede ser accedido mediante el siguiente link:

https://parcialmutantes-martinflores-3k10-2024.onrender.com

### Endpoints

- **POST** /api/mutant/ - Recibe un JSON con la matriz de ADN a verificar. Ejemplo:

```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}
```
- **GET** /api/stats - Devuelve un JSON con la cantidad de mutantes y humanos verificados. Ejemplo:

```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```

## Ejemplos de ADN

Ejemplo de matriz **MUTANTE**:

```json
{
  "dna": [
    "AAAAAA",
    "CCCCAA",
    "TTTTCA",
    "GGTGAA",
    "CCGTAA",
    "AAGTAA"
  ]
}

```

Ejemplo de matriz **NO MUTANTE**:

```json
{
  "dna": [
    "TACGAC",
    "GGTACC",
    "CCTTAG",
    "AGCCTG",
    "CAAACC",
    "GCAGTC"
  ]
}


```

## Entorno Local
Esta API REST esta preparada para utilizar una base de datos H2, para ejecutarla en un entorno local se debera utilizar una copia del Github y correrla. 
El link para levantar la base de datos desde un navegador es: 

http://localhost:8080/h2-console/

Para realizar las mismas operaciones POST y GET se utilizan los endpoints anteriores, estos se aplican en una herramienta de pruebas de API como por ejemplo PostMan.
