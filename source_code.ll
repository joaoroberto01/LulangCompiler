{Teste OK}
programa test1;
var a,b,c,x: inteiro;
{proc 1}
procedimento analisa1;
var x: inteiro;
       z: booleano;
 procedimento analisa2;
 inicio
   leia(a);b:=1;
   b:= a*a+(c div b);
   escreva(b)
 fim;
 funcao func1: inteiro;
 inicio
  analisa2;
  func1:= +a-(-b);
 fim;
inicio
  leia(x);
  z:= verdadeiro;
  se (x>1)ou (+x*(-100)>(-func1))e(nao z)  entao
      inicio
           x:= func1;
           z:= falso;
           escreva(x);
      fim;
  enquanto nao ( (-a*(-b) = 1064) ou (a<= (-1000)) ou z e (nao z))  {condicao de parada}
  faca inicio
           a:= x+b;
           z:= falso ou nao falso
       fim;
  escreva(a)
fim;
inicio
inicio
    leia(c);
    analisa1;
    se b > (c+ a*a)
    entao escreva(b)
    senao escreva(c)
fim
fim.