Prova Projeto Web
===========================

Autor: Daniel Garcia Lamas 

e-mail: danielamas@gmail.com

Formado por duas `Apps`, um `Client`, contendo apenas paginas html, javascrip e [JQuery](https://jquery.com/) e outra `Server` que implementa os `WebServers` restful utilizando [Jersey](https://jersey.java.net/).

Para mapeamento do banco relacional foi utilizado o [Hibernate](http://hibernate.org/orm/) e o banco de dados relacional [MySql 5.7](https://www.mysql.com/) e por fim, para utilizar o Open Movie Database, a API Java [PiM-api-omdb](https://github.com/apoclyps/PiM-api-omdb) foi incorporada ao projeto.

***
Configurando o Banco de Dados
-------------
Foi utilizado a usuário ROOT do banco de dados `MySql`, assim no banco criamos um SCHEMA com o nome `movie`, segue exemplo:

`CREATE SCHEMA movie;`

As tabelas necessárias serão criadas pelo Hibernate.  

***
Configurando o projeto
-------------
Baixe ou clone o projeto para o workspace do Eclipse.

No Eclipse, importe o projeto.

Ainda no Eclipse, na aba `Servers`, adicione o Tomcat 7. Depois, de duplo clique no server adicionado e em `Server Location` selecione a opção `Use Tomcat Installation.`

Abra o `persistence.xml` que se encontra em `apps/webapp/src/META-INF` e altere as seguintes propriedades:

`javax.persistence.jdbc.url` - por padrão foi utilizado o banco na própria maquina onde estava o Tomcat, por isso o endereço `localhost` e a porta padrao `3306`, caso seja necessário basta altera-los;

`javax.persistence.jdbc.user` - configurado inicialmente com user root;

`javax.persistence.jdbc.password` - troque pela senha do seu usuário do `MySql`;

Utilize a ferramente Ant do Eclipse para adicionar os Buildfiles build_client.xml e build_server.xml respectivamente em `/apps/client` e `/apps/webapp`.

Edite os dois Buildfiles, alterando `value` da propriedade `war.deploy`, apontando assim para o endereço da pasta `webapps` do seu Tomcat 7.

De deploy do projeto clicando em `deploy` ou `deploy-exploded` dentro das opções de cada buildfiles.

Na aba `Servers` no Eclipse, inicie o Tomcat.

Inicie o `Chrome` ou o `Firefox`, por exemplo, e na barra de endereço entre com `http://localhost:8080/movieclient/` e comece a utilizar sistema-prova.

***
Loggin do Projeto
---------------
O projeto utliza o [SLF4J](http://www.slf4j.org).
***
Usando o Sistema
---------------

A primeira tela permite buscar filmes e series, listando o resultado logo abaixo do campo de busca.

Clicando em `Detalhes`, vamos para segunda tela que lista mais informações sobre o item e disponibiliza o link `Editar`.

Editando o item, se o mesmo não se encontra no banco local ele é salvo localmente. Edite uma ou mais informações sobre o item, salve clicando no botão salvar e depois volte para a tela de `Detalhes` clicando no botão voltar `<<`.

Editado com sucesso podemos deleta-lo e para isso basta basta clicar no link `Deletar`. Um popup de confirmação é aberto, clique em `OK` para deletar o item e clique no botão voltar `<<` para voltar para tela de busca. 




