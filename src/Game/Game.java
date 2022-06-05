package Game;

import Model.Background;
import Model.Menu;
import Model.NaveInimiga;
import Model.NaveJogador;
import Model.Tiro;
import Principal.Principal;
import javax.swing.JPanel;
import Resource.KeyHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel
{

    //INSTANCIA OBJETO TECLADO DE KEYHANDLER
    KeyHandler teclado = new KeyHandler(); 
    
    //NAVE JOGADOR
    public NaveJogador navejogador;
    
    //NAVE INIMIGA
    public NaveInimiga naveinimiga;
    
    //LIST NAVE INIMIGA
    private List<NaveInimiga> navesinimigas;
    
    //BACKGROUND
    public Background backgrounda;
    public Background backgroundb;     
    
    //VARIAVEL BOOLEANA ISRODANDO INICIALIZADA TRUE;
    private boolean isRodando = true;    
    
    //VARIAVEL GAMESTATE INICIALIZADA COMO MENU
    public static String gamestate = "menu";
    
    //VARIAVEL MENU
    public static Menu menu;
    
    //VARIAVEL THREAD DO TIPO THREAD
    Thread thread;
    
    //VARIAVEL FPS INICIALIZADA COM 0
    int fps = 0;
    
    //CONSTRUTOR
    public Game()
    {
        //INSTANCIA NAVE DO JOGADOR 
        navejogador = new NaveJogador();
        
        //INSTANCIA BACKGROUND A E BACKGROUND B 
        backgrounda = new Background();
        backgroundb = new Background();      
        
        
        //REPOSICIONA FUNDO B
        backgroundb.posX = backgroundb.posX + 1024;
        
        //SETA VELOCIDADE DO BACKGROUND
        backgrounda.velX = backgrounda.velX - 6;
        backgroundb.velX = backgroundb.velX - 6;        
  
        //INSTANCIA MENU
        menu = new Menu(0, 0, 0, 0, null);
        
        //ISNTANCIA NAVE INIMIGA
        naveinimiga = new NaveInimiga();        
        
        addKeyListener(teclado);
        
        setDoubleBuffered(true);
        
        //RECEBE FOCO
        setFocusable(true);
        
        //THREAD INICIA GAMELOOP(RUNNING)
        new Thread(new Runnable()
        {@Override
         public void run()
         {
             long lastTime = System.nanoTime();
             double amountofTicks = 60.0;
             double ns = 1000000000 / amountofTicks;
             double delta = 0;
             int frames = 0;
             double timer = System.currentTimeMillis();
             while(isRodando)
             {
                 
                 long now = System.nanoTime();
                 delta +=(now - lastTime)/ns;
                 lastTime = now;
                 if(delta>= 1)
                 {
                     
                    handlerEvents();
                    update();
                    render();
                     
                    frames++;
                    delta--;
                 }    
                 
                 
                 if(System.currentTimeMillis()- timer>=1000)
                 {
                     
                     System.out.println("FPS: "+ frames);
                     fps = frames;
                     frames = 0;
                     timer+=1000;
                     
                 }    
             }    
             
         }        
        }).start();
        
        
    }        

    //METODO PARA INICIAR O JOGO
    public synchronized void start()
    {
        
        thread = new Thread((Runnable) this);
        isRodando = true;
        thread.start();
        stop();    
        
        
    }        
    
    //METODO PARA PARAR O JOGO
    public synchronized void stop()
    {
        
        isRodando = false;
        try
        {
            
            thread.join();
            
        }
        catch(InterruptedException e)
        {
            
            e.printStackTrace();
            
        }    
        
    }        
    
    //AÇÕES DO JOGADOR
    public void handlerEvents()
    {
        
        //se teclado cima apertado = true
        if(teclado.CimaPressed ==  true)
        {
                    
            navejogador.posY -= navejogador.velY;
                    
        }   
                
        //se teclado baixo apertado = true
        if(teclado.BaixoPressed ==  true)
        {
                    
            navejogador.posY += navejogador.velY;
                    
        }                  
                
        //se teclado esquerda apertado = true
        if(teclado.EsquerdaPressed ==  true)
        {
                    
            navejogador.posX -= navejogador.velX;

                    
        }  
                
        //se teclado direita apertado = true
        if(teclado.DireitaPressed ==  true)
        {
                    
            navejogador.posX += navejogador.velX;
                    
        }                                   
    } 
    
    //METODO PARA ATUALIZAR FRAMES
    public void update()
    {
        //SE O GAMESTATE FOR IGUAL A GAME
        if(gamestate == "game")
        {
            
            navejogador.mudarFrame();
        
            //TESTE FUNDO
            testeFundo();
        
            //TESTE COLISAO TELA
            testeColisaoTela();
            
            //MOVIMENTO ALEATORIO NAVE INIMIGA
            iniciainimigos();
            
            //INICIA TIRO
            iniciaTiros();
        
            //MOVIMENTO DO BACKGROUND
            backgrounda.posX += backgrounda.velX;
            backgroundb.posX += backgroundb.velX;   
            
            //PARA I MENOR QUE TAMANHO LISTA NAVE INIMIGA
            for(int i = 0; i < navesinimigas.size(); i++)
            {
                //NAVE INIMIGA NI RECEBE O INDICE DA LISTA DA NAVE INIMIGA
                NaveInimiga ni = navesinimigas.get(i);
                //SE NAVE INIMIGA É VISIVEL
                if(ni.isIsVisivel())
                {
                    //NAVE INIMIGA ATUALIZE
                    ni.update();
                    
                }
                else
                {
                    //REMOVA DA LISTA
                    navesinimigas.remove(i);
                    
                }    
                
            }    
            
            
        } 
        //SE GAMESTATE FOR IGUAL A MENU
        else if (gamestate == "menu")
        {
            //CHAME MENU UPDATE
            menu.update();
            
        }    
        
    }
    
    public void render()
    {
        
        repaint();
        
    }
    
    //METODOS         
    public void iniciaTiros()
    {
            //LISTA DE TIROS TIROS RECEBE NAVEJOGADOR GET TIROS
            List<Tiro> tiros = navejogador.getTiros();
            
            //PARA I MENOR QUE TAMANHO LISTA TIROS
            for(int i = 0; i < tiros.size(); i++)
            {
                //TIRO T PARSE PARA TIRO PEGUE O INDICE LISTA TIROS
                Tiro t = (Tiro)tiros.get(i); 
                
                //SE VISIVEL
                if(t.isVisivel)
                {
                    //TIRO ATUALIZE
                    t.update();
                    
                } 
                else
                {
                    //LISTA TIROS REMOVA E DECREMENTE I
                    tiros.remove(i);
                    i--;
                }    
                
            }            
        
    }        
    
    //METODO PARA INICIAR OS INIMIGOS
    public void iniciainimigos()
    {
        
        int cordenadas[] = new int [40];
        navesinimigas = new ArrayList<NaveInimiga>();
        
        for(int i = 0; i < cordenadas.length; i++)
        {
            
            int x = (int)(Math.random()*8000+1022);
            int y = (int)(Math.random()*600+30);
            
            navesinimigas.add(new NaveInimiga(x,y));
            
        }    
        
    }        
    
    //REPOSICIONAR FUNDO
    public void testeFundo()
    {
        
        if((backgrounda.posX+1024) <= 0)//SE SAIU PELA ESQUERDA DA TELA
        {
            
            //REPOSICIONA BACKGROUNDA PARA TRAS DO BAKGROUNDB
            backgrounda.posX = backgroundb.posX + 1024;
            
        }	
        if((backgroundb.posX+1024) <= 0)//SE SAIU PELA ESQUERDA DA TELA
        {
            
            //REPOSICIONA BACKGROUNDA PARA TRAS DO BAKGROUNDB
            backgroundb.posX = backgrounda.posX + 1024;
            
        }           
        
    }        
    
    //IMPEDE NAVE DE FUGIR DA AREA DO JOGO
    public void testeColisaoTela()
    {
        
        if(navejogador.posX+(navejogador.raio*2) >= Principal.LARGURA_TELA)
        {
            
            navejogador.posX = navejogador.posX - navejogador.velX;
            
        } 
        
        if(navejogador.posX <= 0)
        {
            
            navejogador.posX = navejogador.posX + navejogador.velX;
            
        }   
        
        if(navejogador.posY+(navejogador.raio*2) >= Principal.ALTURA_TELA )
        {
            
            navejogador.posY = navejogador.posY - navejogador.velY;
            
        }
        
        if(navejogador.posY <= 0)
        {
            
            navejogador.posY = navejogador.posY + navejogador.velY;
            
        }   
    }    
    
    
    
    //METODOS ESPECIAIS
    public void paintComponent(Graphics g)
    {
        
        super.paintComponent(g);
        
        //SE GAMESTATE FOR IGUAL A GAME
        if(gamestate == "game")
        {
            g.drawImage(backgrounda.imagem, backgrounda.posX, backgrounda.posY, null);
            g.drawImage(backgroundb.imagem, backgroundb.posX, backgroundb.posY, null);         
            g.drawImage(navejogador.imagAtual, navejogador.posX, navejogador.posY, null);  
            
            //LISTA TIRO TIROS RECEBE NAVEJOGADOR GET TIROS
            List<Tiro> tiros =  navejogador.getTiros();
            
            //PARA I MENOR QUE TAMANHO LISTA TIROS
            for(int i = 0; i < tiros.size(); i++)
            {
                //TIRO T PARSE TIRO PEGUE INDICE LISTA TIROS
		Tiro t = (Tiro)tiros.get(i); 
                //DESENHA
		g.drawImage(t.getImagem(), t.getPosX(), t.getPosY(), this);                
                
            }   
            
            List<NaveInimiga> navesinimigas = naveinimiga.getNavesInimigas();
            
            for(int i = 0; i < navesinimigas.size(); i++)
            {
                
                NaveInimiga navei = navesinimigas.get(i);
                
                g.drawImage(navei.getImagAtual(), navei.getPosX(), navei.getPosY(), null);    
                
                
            }              
            
            //CONTADOR FPS
            g.setColor(new Color(110,200,10));
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("FPS: "+fps, 5, 20);
        }
        //SE NAO SE GAMESTATE FOR IGUAL A MENU
        else if(gamestate == "menu")
        {
            //CHAME MENU PAINTCOMPONENT
            menu.paintComponent(g);
            
        }    
        
    }        
}

