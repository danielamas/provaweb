Prova Projeto Web
===========================

Autor: Daniel Garcia Lamas 

e-mail: danielamas@gmail.com

Formado por duas `Apps`, um `Client`, contendo apenas paginas html, javascrip e [JQuery](https://jquery.com/) e outra `Server` que implementa os `WebServers` restful utilizando [Jersey](https://jersey.java.net/).

Para mapeamento do banco relacional foi utilizado o [Hibernate](http://hibernate.org/orm/) e o banco de dados relacional [MySql 5.7](https://www.mysql.com/) e por fim, para utilizar o Open Movie Database, a API Java [PiM-api-omdb](https://github.com/apoclyps/PiM-api-omdb) foi incorporada ao projeto.

***
Configurando o Banco de Dados
-------------
Foi utilizado a usu�rio ROOT do banco de dados `MySql`, assim no banco criamos um SCHEMA com o nome `movie`, segue exemplo:

`CREATE SCHEMA movie;`

As tabelas necess�rias ser�o criadas pelo Hibernate.  

***
Configurando o projeto
-------------
Baixe ou clone o projeto para o workspace do Eclipse.

No Eclipse, importe o projeto.

Ainda no Eclipse, na aba `Servers`, adicione o Tomcat 7. Depois, de duplo clique no server adicionado e em `Server Location` selecione a op��o `Use Tomcat Installation.`

Abra o `persistence.xml` que se encontra em `apps/webapp/src/META-INF` e altere as seguintes propriedades:

`javax.persistence.jdbc.url` - por padr�o foi utilizado o banco na pr�pria maquina onde estava o Tomcat, por isso o endere�o `localhost` e a porta padrao `3306`, caso seja necess�rio basta altera-los;

`javax.persistence.jdbc.user` - configurado inicialmente com user root;

`javax.persistence.jdbc.password` - troque pela senha do seu usu�rio do `MySql`;

Utilize a ferramente Ant do Eclipse para adicionar os Buildfiles build_client.xml e build_server.xml respectivamente em `/apps/client` e `/apps/webapp`.

Edite os dois Buildfiles, alterando `value` da propriedade `war.deploy`, apontando assim para o endere�o da pasta `webapps` do seu Tomcat 7.

De deploy do projeto clicando em `deploy` ou `deploy-exploded` dentro das op��es de cada buildfiles.

Na aba `Servers` no Eclipse, inicie o Tomcat.

Inicie o `Chrome` ou o `Firefox`, por exemplo, e na barra de endere�o entre com `http://localhost:8080/movieclient/` e comece a utilizar sistema-prova.

***
Loggin do Projeto
---------------
O projeto utliza o [SLF4J](http://www.slf4j.org).
***
Usando o Sistema
---------------

A primeira tela permite buscar filmes e series, listando o resultado logo abaixo do campo de busca.

Clicando em `Detalhes`, vamos para segunda tela que lista mais informa��es sobre o item e disponibiliza o link `Editar`.

Editando o item, se o mesmo n�o se encontra no banco local ele � salvo localmente. Edite uma ou mais informa��es sobre o item, salve clicando no bot�o salvar e depois volte para a tela de `Detalhes` clicando no bot�o voltar `<<`.

Editado com sucesso podemos deleta-lo e para isso basta basta clicar no link `Deletar`. Um popup de confirma��o � aberto, clique em `OK` para deletar o item e clique no bot�o voltar `<<` para voltar para tela de busca. 




