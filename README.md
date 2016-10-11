# Hash
Trabalho de Organizacao de Estruturas de Dados

O arquivo foi elaborado com o objeto de criar uma hash para fazer uma lista encadeada de uma lista de CEPs e fazer com que a busca no mesmo seja realizada mais rapida.

Usando como referencias os valores de hash = 900001

O arquivo Principal.java possui todas as funcoes para realizar o arquivo.

Input :  arquivo cep_ordenado.dat dado pelo professor.

Output: Cria um arquivo hash.dat - com a lista encadeada

OBS: Todas as funcoes tem comentarios explicando o passo a passo do arquivo para tornar mais simples a coomprensao.

Funcionamento

Existe um menu no arquivo que direciona o usuario para utilizar as funcoes.

1-Cria a hash.
2- Faz Busca de Cep
3-Estatisticas
4-Sai do Arquivo

1---- A funcao cria hash, cria um arquivo preenche ele com -1 em 3 colunas(Posicao, CEP e Proximo). Depois abre o arquivo de CEP original e divide o CEP com o valor da hash desejada. Apos isso pega o resultado e grava o CEP, a posicao dele no arquivo original. Se o valor do CEP for -1 ele esta vazio entao grava o seu valor. Se nao vai para o proximo e segue a lista ate encontrar um vazio e o adiciona no fim do arquivo.

2---- A funcao buscarCep pega o CEP e divide pelo valor da hash, com este resultado vai direto na posicao que o arquivo deve estar. Se nao localiza de primeira vai para a posicao proximo e assim por sequencia ate achar o valor do cep. Depois pega a posicao do arquivo original que esta grava nele e abre o arquivo original direto no ponteiro dele. Entao pega os dados restantes do CEP.

3--- Tras algumas estatisticas interessantes sobre o arquivo.

Que sao calculadas com base nos dados considerando uma hash de 900001.

Estatisticas

Existem 502464 espacos vazios no array
O numero medio de passos sera  1.7665303 passos 
O espaco com mais CEPS possui 15 enderecos

Alem dessas estatisticas o arquivo traz tambem a probabilidade de achar outros CEPS e sua quantidade de elementos encadeados.
