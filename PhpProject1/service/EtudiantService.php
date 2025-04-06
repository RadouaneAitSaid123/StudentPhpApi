<?php

include_once RACINE . '/classes/Etudiant.php';
include_once RACINE . '/connexion/Connexion.php';
include_once RACINE . '/dao/IDao.php';

class EtudiantService implements IDao {

    private $connexion;

    function __construct() {
        $this->connexion = new Connexion();
    }

    #[\Override]
    public function create($o) {
        $query = "INSERT INTO Etudiant (id, nom, prenom, ville, sexe, date_naissance, image) 
              VALUES (NULL, :nom, :prenom, :ville, :sexe, :date_naissance, :image)";

        // Préparation de la requête
        $req = $this->connexion->getConnexion()->prepare($query);

        // Assigner les valeurs des méthodes de l'objet à des variables
        $nom = $o->getNom();
        $prenom = $o->getPrenom();
        $ville = $o->getVille();
        $sexe = $o->getSexe();
        $date_naissance = $o->getDate_naissance();
        $image = $o->getImage();

        // Utilisation de bindParam pour lier les valeurs à la requête
        $req->bindParam(':nom', $nom);
        $req->bindParam(':prenom', $prenom);
        $req->bindParam(':ville', $ville);
        $req->bindParam(':sexe', $sexe);
        $req->bindParam(':date_naissance', $date_naissance);

        // Utilisation de PDO::PARAM_LOB pour l'image (si elle existe)
        if ($image !== null) {
            $req->bindParam(':image', $image, PDO::PARAM_LOB);
        } else {
            // Si pas d'image, on lie un NULL pour l'image
            $null = null;
            $req->bindParam(':image', $null, PDO::PARAM_LOB);
        }

        // Exécution de la requête
        $req->execute() or die('Erreur SQL');
    }

    #[\Override]
    public function delete($o) {
        $query = "delete from Etudiant where id = " . $o->getId();
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute() or die('Erreur SQL');
    }

    #[\Override]
    public function findAll() {
        $etds = array();
        $query = "select * from Etudiant";
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute();
        while ($e = $req->fetch(PDO::FETCH_OBJ)) {
            // $etds[] = new Etudiant($e->id, $e->nom, $e->prenom, $e->ville, $e->sexe);
        }
        return $etds;
    }

    #[\Override]
    public function findById($id) {
        $query = "select * from Etudiant where id = " . $id;
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute();
        $e = $req->fetch(PDO::FETCH_OBJ);
        if ($e) {
            $etd = new Etudiant($e->id, $e->nom, $e->prenom, $e->ville, $e->sexe, $e->date_naissance, $e->image);
            return $etd;
        } else {
            return null; 
        }
    }

    #[\Override]
    public function update($o) {
        $query = "UPDATE `etudiant` SET `nom` = '" . $o->getNom() . "', `prenom` = '" .
                $o->getPrenom() . "', `ville` = '" . $o->getVille() . "', `sexe` = '" .
                $o->getSexe() . "' WHERE `etudiant`.`id` = " . $o->getId();
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute() or die('Erreur SQL');
    }

    public function findAllApi() {
        $query = "select * from Etudiant";
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute();
        return $req->fetchAll(PDO::FETCH_ASSOC);
    }
}
