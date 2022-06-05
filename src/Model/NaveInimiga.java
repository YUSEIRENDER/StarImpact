package Model;

import Resource.Recursos;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class NaveInimiga {
    //SPRITE DA NAVE INIMIGA

    public BufferedImage sprite;

    //CADA FRAMES DO SPRITE
    public BufferedImage[] listaFrames;

    //ATRIBUTOS DO SPRITE
    int indiceAtual;
    int qtdLinhasFrame;
    int qtdColunasFrame;
    int qtdFrames;
    public BufferedImage imagAtual;

    long startTime, endTime, deltaTime, tempoDecorrido, intervaloEntreFrames;

    //ATRIBUTOS DA NAVE JOGADOR
    public int posX;
    public int posY;
    public int raio;
    public int velX;
    public int velY;
    public int width;
    public int height;

    private boolean isVisivel;

    public static List<NaveInimiga> navesinimigas; 
    
    //CONSTRUTOR
    public NaveInimiga() {
        //INICIALIZA ARRAY DE FRAMES E OUTROS ATRIBUTOS
        qtdLinhasFrame = 5;
        qtdColunasFrame = 1;
        qtdFrames = qtdLinhasFrame * qtdColunasFrame;
        listaFrames = new BufferedImage[qtdFrames];
        indiceAtual = 0;
        width = 100;
        height = 100;
        posX = 885;
        posY = 100;
        raio = 50;
        velX = -4;
        velY = -4;

        tempoDecorrido = 0;
        intervaloEntreFrames = 20;
        
        navesinimigas = new ArrayList<NaveInimiga>();
        
        //FAZ O CARREGAMENTO DA IMAGEM
        try {
            sprite = ImageIO.read(getClass().getResource("/Imagens/naveinimigaspritesheet.png"));
            
            //PERCORRE AS 5 LINHAS(0-4)
            for (int i = 0; i < qtdLinhasFrame; i++) {
                //PERCORRE A 1 COLUNA (0)
                for (int j = 0; j < qtdColunasFrame; j++) {

                    int x1 = j * width;
                    int y1 = i * height;
                    int x2 = x1 + width;
                    int y2 = y1 + height;
                    listaFrames[i * qtdColunasFrame + j] = Recursos.getInstance().cortarImagem(x1, y1, x2, y2, sprite);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //CONSTRUTOR APENAS COM POSICAO X E Y DA NAVE INIMIGA    
    public NaveInimiga(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

    }

    public void update() {

        this.posX -= velX;
        if (this.posX < 0) {

            isVisivel = false;

        }

    }

    //METODO PARA ATUALIZAR SPRITES A CADA FRAME
    public void mudarFrame() {

        startTime = System.currentTimeMillis();
        deltaTime = startTime - endTime;
        tempoDecorrido = tempoDecorrido + deltaTime;
        if (tempoDecorrido >= intervaloEntreFrames) {

            indiceAtual++;
            tempoDecorrido = 0;

        }
        if (indiceAtual >= qtdFrames - 1) {
            indiceAtual = 0;
        }
        imagAtual = listaFrames[indiceAtual];

        endTime = startTime;

    }
    //GETTERS E SETTERS
    public static List<NaveInimiga> getNavesInimigas() 
    {
	return navesinimigas;
    }    
    
    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public BufferedImage getImagAtual() {
        return imagAtual;
    }

    public void setImagAtual(BufferedImage imagAtual) {
        this.imagAtual = imagAtual;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isIsVisivel() {
        return isVisivel;
    }

    public void setIsVisivel(boolean isVisivel) {
        this.isVisivel = isVisivel;
    }
    
}
