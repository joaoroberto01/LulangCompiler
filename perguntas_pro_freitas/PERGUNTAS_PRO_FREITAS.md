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


atualmente esta permitindo declarar variavel com nome de proc ja definido, e proc com nome de variavel:
![Lulang](p2.png)
------------------------------------------------
x > y ou x < y
![Lulang](p3.png)

analyzeSimpleExpression:273

while (currentToken.is(Token.SMAIS) || currentToken.is(Token.SMENOS) || currentToken.is(Token.SOU) ||
currentToken.is(Token.SMAIOR) || currentToken.is(Token.SMAIORIG) || currentToken.is(Token.SIG)
|| currentToken.is(Token.SMENOR) || currentToken.is(Token.SMENORIG)
|| currentToken.is(Token.SDIF) ) {
----------------------------------------
por que nao é permitido 'verdadeiro = verdadeiro'?
programa test1;

var a,b,c: inteiro;

inicio
se verdadeiro = verdadeiro  entao
b:= 0
senao escreva(b)
fim.

{fim}