{AB}
programa test;
var a,b,s: inteiro;

funcao soma: inteiro;
var c,a,s: inteiro;
    z: booleano;
procedimento ler;
inicio
    leia (c);
    leia (a);
    s:= soma;
    escreva (s);
fim;

procedimento loop;
var x: inteiro;
inicio
    leia (x); {para parar digite um valor maior que 9 }
    se x < 10
    entao ler;
fim;

inicio {corpo da funcao soma}
    loop;
    soma:= c+a;
fim;

inicio {corpo principal}
    s:= soma;
    escreva(s)
fim.