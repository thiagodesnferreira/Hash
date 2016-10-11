import java.io.RandomAccessFile;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.DataOutput;
import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Principal {

	public boolean checkHash()throws Exception
	{
		boolean t =false;
		String nomearquivo = "hash.data";
		File f = new File(nomearquivo);
		if(f.exists() && !f.isDirectory()) { 
			System.out.println("Otimo, voce ja tem o hash criado!");
		}
		else
		{
			System.out.println("Voce ainda nao criou um arquivo de hash. Vamos cria-lo");
			//criarHash();
		}
		
		return t ; 
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static void buscarCep()throws Exception
	{
		//Solicita  cep  ser buscado
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Digite o Cep a ser buscado");
		String cep = keyboard.next();
		//checa se eh numerico
		while(!isNumeric(cep))
		{
			System.out.println("Digite um numero valido");
			cep= keyboard.next();
								
		}
		//Pede nome do arquivo
		System.out.println("Digite o nome do arquivo base para a busca do Hash (Ex:cep_ordenado.dat)");
		String nomehash = keyboard.next();
		File arq = new File(nomehash);
			//Checa se o arquivo existe
		while(!(arq.exists() && !arq.isDirectory()))
		{
			System.out.println("Arquivo nao encontrado. Digite o endereco correto.");
			nomehash = keyboard.next();
			arq = new File(nomehash);					
		}
		
		//Pede numero da Hash usada como chave.
		System.out.println("Digite o numero do Hash");
		String nhash = keyboard.next();
		
		//testa se e numero
		while(!isNumeric(nhash))
		{
			System.out.println("Digite um numero valido");
			nhash = keyboard.next();
								
		}
		
		int hashnumber = Integer.parseInt(nhash);
		
		//Acessa o arquivo para criar a hash
		RandomAccessFile f = new RandomAccessFile(nomehash, "r");
		RandomAccessFile lista = f;
		
		//Acessa o arquivo a ser criado
		RandomAccessFile n = new RandomAccessFile("hash.dat", "rw");
		
		int buscacep=Integer.parseInt(cep);
		int hashvalue = buscacep % hashnumber;
		
		//posiciona o arquivo na posicao do hash dado
		n.seek(hashvalue*24);
		long pos = n.readLong();
		long cepid = n.readLong();
		long prox = n.readLong();
		// porem caso seja um segundo elemento vai proculalo no encadeamento
		while(cepid != buscacep && prox != -1 )
		{
			n.seek(prox);
			pos = n.readLong();
			cepid = n.readLong();
			prox = n.readLong();
		}
		//se for igual o cep entao ele verifica se foi realmente encontrado ou so o proximo que foi -1 indicando valor vazio
		if(cepid == buscacep)
		{
			//vai para o arquivo principal e cria o hash
			f.seek(pos*300);
			Endereco e = new Endereco();
            e.leEndereco(f);
            //exibe o resultado
            System.out.println(e.getLogradouro());
            System.out.println(e.getBairro());
            System.out.println(e.getCidade());
            System.out.println(e.getEstado());
            System.out.println(e.getSigla());
            System.out.println(e.getCep());  
		}
		
	}
	public static void criarHash()throws Exception
	{
				//Pede nome arquivo base para criar o hash
				Scanner keyboard = new Scanner(System.in);
				System.out.println("Vamos criar um arquivo de Hash.");
				System.out.println("Digite o nome do arquivo base para criacao do Hash (Ex:cep_ordenado.dat)");
				String nomehash = keyboard.next();
				File arq = new File(nomehash);
					//Checa se o arquivo existe
				while(!(arq.exists() && !arq.isDirectory()))
				{
					System.out.println("Arquivo nao encontrado. Digite o endereco correto.");
					nomehash = keyboard.next();
					arq = new File(nomehash);					
				}
				
				//Pede numero da Hash usada como chave.
				System.out.println("Digite o numero do Hash");
				String nhash = keyboard.next();
				
				//testa se e numero
				while(!isNumeric(nhash))
				{
					System.out.println("Digite um numero valido");
					nhash = keyboard.next();
										
				}
				
				int hashnumber = Integer.parseInt(nhash);
				
				
				
				//Acessa o arquivo para criar a hash
				RandomAccessFile f = new RandomAccessFile(nomehash, "r");
				RandomAccessFile lista = f;
				
				//Acessa o arquivo a ser criado
				RandomAccessFile n = new RandomAccessFile("hash.dat", "rw");
				
				
				//Pega o tamanho do Cep total e faz um loop para ler todos os dados
				long fim = lista.length()/300;
				int cepbusca;
				Endereco busca = new Endereco();
				int aux;
				int hashvalue ;
				int t =0;
				long pos; // variavel que guarda a posicao do cep
				long cepid; //variavel que guarda o cep
				long prox; // variavel que mostra o proximo
				
				//variavel para manter o usuario ciente que esta ocorrendo a escrita
				long p=0;
				long next=1;
				
				//cria o arquivo hash ou o reescreve com 3 longs fazendo a posicao dos atributos
				System.out.println("Criando o arquivo da hash..");
				System.out.println("1%");
				while(t<hashnumber)
				{
					
					p=(t*100)/hashnumber;
					if(p>next)
					{
						System.out.println(p+"%");
						next=next+1;
						
					}
					
					n.writeLong(-1);//Posicao Cep
					n.writeLong(-1);// Cep
					n.writeLong(-1);// Proximo
					t++;
				}
				
				//zera os valores para usar no proximo loop
				t=0;
				p=0;
				next=1;
				System.out.println("Criando a Hash com elementos do arquivo"+nomehash);
				System.out.println("1%");
				while(t<fim)
				{	
					p=(t*100)/fim;
					if(p>next)
					{
						System.out.println(p+"%");
						next=next+1;
					}
					//dout.writeLong(this.cep);
					
					//Pega o valor do CEP e acha o hash
					busca.leEndereco(lista);
					cepbusca = Integer.parseInt(busca.getCep());
					hashvalue = cepbusca % hashnumber;
					
					// Posiciona na posicao do hash referente ao valor.
					//Files/cep_ordenado.dat
					n.seek(hashvalue*24);
					pos = n.readLong();
					cepid = n.readLong();
					prox = n.readLong();
					//pega os valores da lista atual
					long cep = Long.parseLong(busca.getCep());
					
					//se o cepid estiver igual a -1 significa que o array esta vazio.
										
					if(cepid == -1)
					{
						//reposiciona o ponteiro do leitor
						n.seek(hashvalue*24);
						//escreve os valores
						n.writeLong(t);//Posicao Cep
						n.writeLong(cep);// Cep
						n.writeLong(-1);// Proximo
					}
					else
					{
						//reposiciona o ponteiro do leitor
						n.seek(hashvalue*24);
						//reescreve os valores para chegar no proximo e registrar o proximo como  o valor da sua futura posicao, o final da lista
						n.writeLong(pos);//Posicao Cep
						n.writeLong(cepid);// Cep
						n.writeLong(n.length());// Proximo
						n.seek(n.length());
						//agora escreve o valor do cep todo
						n.writeLong(t);//Posicao Cep
						n.writeLong(cep);// Cep
						n.writeLong(-1);// Proximo
						
					}
					//hashList.set(hashvalue, aux);
					
					
					t++;
				}
				
				
	}
		
	
	public static void estatisticas()throws Exception 
	{
		
		System.out.println("Vamos calcular as estatisticas.");
		//Pede nome arquivo base para criar o hash
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Vamos criar um arquivo de Hash.");
		System.out.println("Digite o nome do arquivo do Cep(Ex:cep_ordenado.dat)");
		String nomehash = keyboard.next();
		File arq = new File(nomehash);
			//Checa se o arquivo existe
		while(!(arq.exists() && !arq.isDirectory()))
		{
			System.out.println("Arquivo nao encontrado. Digite o endereco correto.");
			nomehash = keyboard.next();
			arq = new File(nomehash);					
		}
		
		//Pede numero da Hash usada como chave.
		System.out.println("Digite o numero do Hash");
		String nhash = keyboard.next();
		
		//testa se e numero
		while(!isNumeric(nhash))
		{
			System.out.println("Digite um numero valido");
			nhash = keyboard.next();
								
		}
		
		int hashnumber = Integer.parseInt(nhash);
		
		RandomAccessFile f = new RandomAccessFile(arq, "r");
		RandomAccessFile lista = f;
		
		//Cria uma Lista encadeada com o numero do hash
		ArrayList< Integer > hashList = new ArrayList<>();
		int cepbusca ;
		long fim = lista.length()/300;
		Endereco busca = new Endereco();
		int aux;
		int hashvalue ;
		
		//variavel para manter o usuario ciente que esta ocorrendo a escrita
		long p=0;
		long next=1;
		
		//cria o arquivo hash ou o reescreve com 3 longs fazendo a posicao dos atributos
		System.out.println("Criando o array de hash com Zero`s..");
		System.out.println("1%");
			
			
		//cria um array list com valores 0
		for(int i=0; i < hashnumber ; i++)
		{
			p=(i*100)/hashnumber;
			if(p>next)
			{
				System.out.println(p+"%");
				next=next+1;
				
			}
			
			hashList.add(0);
			
		}
		int t =0;
		p=0;
		next=1;
		
		System.out.println("Somando um no array correspondente a hash..");
		System.out.println("1%");
		// Soma um em cada casa que estiver com um hash 
		while(t<fim)
		{
			p=(t*100)/fim;
			if(p>next)
			{
				System.out.println(p+"%");
				next=next+1;
				
			}
			
			busca.leEndereco(lista);
			cepbusca = Integer.parseInt(busca.getCep());
			hashvalue = cepbusca % hashnumber;
			aux = hashList.get(hashvalue);
			//adiciona um ao valor do hash
			aux= aux+1;
			hashList.set(hashvalue, aux);
			aux=0;
			t++;
		}
		
		
		int i=0;
		int numbzero=0;
		int maior =0;
		System.out.println("Contando o numero de colisoes..");
		// conta o Array que mais tem colisoes
		int  totaldepassos = 0;
		//variavel para saber quantos registros tem em cada hash.
		int [] numeromax = new int [1000];
		for(int g=0; g < 100 ; g++)
		{
			numeromax[g]=0;
		}
		
		while(i < hashList.size())
		{
			
			numeromax[hashList.get(i)]= numeromax[hashList.get(i)] + 1; 
			
			//Soma um no total se a casa do array estiver vazia
			if(hashList.get(i) == 0)
			{
				
				numbzero=numbzero+1;
				
			}
			else
			{
				//Soma ao numero total de passos que vai demorar para achar cada array
				
				
			}
			for(int y=0; y <hashList.get(i) ; y++)
			{
				totaldepassos = totaldepassos + y + 1  ; 
				
				
			}
			
			//se for o elemnto com mais colisoes muda para o maior
			if(hashList.get(i) > maior)
			{
				maior = hashList.get(i);
				System.out.println("Por enquanto o array com mais colisoes possui - "+maior+" colisoes");
			}
			i++;
			
			
		}
		
		ArrayList< Integer > hashList2 = new ArrayList<>();
		//a probabilidade para achar o registro eh o numero total de passos divido pelo numero de registro
		
		float val = 0.0f;
		//calculando quantos passos tem em cada no max
		int cascheia = hashList.size() - numbzero ; 
		float totreg=0;
		
		//total de registros e casos
		totreg=0;
		for(int j=0 ; j < numeromax.length ; j++)
		{
			
			if(numeromax[j] > 0 && j >0)
			{
				for(int y=j; y <numeromax.length ; y++)
				{
					numeromax[j] = numeromax[j]+numeromax[y];
					
				}
				totreg = totreg + (numeromax[j]);
				
				val = (float)((float) numeromax[j])/(totreg);
				if(val > 0.001f)
				{
					System.out.println("Existem "+ numeromax[j]+" com "+j+" registros - Possibilidade para achar na vez - " +j+ " sera de "+ val );
				}
				else
				{
					System.out.println("Existem "+ numeromax[j]+" com "+j+" registros - Possibilidade para achar na vez - " +j+ " sera  que 0.001");
				}
				
				
			}
			
			
			
		}
		
		System.out.println("Existem " + numbzero + " espacos vazios no array");
		System.out.println("O numero medio de passos sera  " + (float)((float)totaldepassos)/fim + " passos ") ;
		System.out.println("O espaco com mais CEPS possui " + maior + " enderecos");
		
		
		
	}
	
	public static void main(String[] args) throws Exception 
	{
		//Cria loop do menu 
		int t = 1 ;
		
		while(t==1)
		{
			//Solicita comando para ser executado
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Selecione uma das opcoes abaixo : ");
			System.out.println("1 - Criar Arquivo 'hash.data' ");
			System.out.println("2 - Buscar CEP");
			System.out.println("3 - Estatisticas");
			System.out.println("4 -  Sair do arquivo");
			String id = keyboard.next();
			
			
			
			if(id.equals("1"))
			{
				criarHash();
				
			}
			else if(id.equals("2"))
			{
				System.out.println("Vamos buscar um cep no hash");
				
				//Verifica se o hash ja foi criado 
				String nomearquivo = "hash.dat";
				File f = new File(nomearquivo);
				if(f.exists() && !f.isDirectory()) { 
					System.out.println("Otimo, voce ja tem o hash criado!");
					buscarCep();
				}
				else
				{
					System.out.println("Voce ainda nao criou um arquivo de hash. Vamos cria-lo");
					//chama criar arquivo hash
				}
				
			}
			else if(id.equals("3"))
			{
				
				estatisticas();
				
			}
			else if(id.equals("4"))
			{
				
				System.out.println("Obrigado por usar o gerador de hash.");
				t=2;
				
			}
			else
			{
				
				System.out.println("Selecione a opcao : Digite 1, 2, 3 ou 4.");
				
			}
			
			
			
		}
		
		
		
		
	}
	
	

}
