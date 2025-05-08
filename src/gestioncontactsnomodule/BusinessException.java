package gestioncontactsnomodule;

public class BusinessException extends Exception {
 // Premier constructeur : prend un message d'erreur en paramètre
    public BusinessException(String message) { 
        super(message);
    }
 // Deuxième constructeur : prend un message d'erreur + une autre exeption
    public BusinessException(String message, Throwable cause) {
        super(message, cause); ////  appeler le constructeur de la classe parente
    }
}