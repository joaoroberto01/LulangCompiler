.
rotulo precisa ser LX (L1) ou é so um exemplo?
---------------
.
programa entra na tabela de simbolos... oq colocar no endereço? nada? -1? 777?
---------------
Vai dar overlap de variavel?
![Lulang](p1.png)

---------------
.
e sobre comentarios no nosso codigo? precisa?
----------------
isso nao pode 
funcao soma: inteiro;  
inicio  
....leia(soma)  
fim; 

isso pode
funcao teste: inteiro;
    funcao soma: inteiro;
    var teste;
    inicio  
    ....leia(teste)  
    fim;

pode isso arnaldo? ou precisa chamar leia(x) e atribuir a variavel soma (retorno de funcao)  
**SE PUDER, hasVarDeclaration dentro de analyzeRead precisa buscar por funcoes tambem


var x,y;
      z; 
ALLOC 0, 2
ALLOC 2, 1

ou ALLOC 0,3 ?????


matheus nao esta totalmente confiante da soluçao

ALLOc 0,2
ALLOC 2,1

...
DALLOC 3, 3