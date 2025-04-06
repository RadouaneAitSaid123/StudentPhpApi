<?php

class Etudiant {

    private $id;
    private $nom;
    private $prenom;
    private $ville;
    private $sexe;
    private $date_naissance;
    private $image;

    public function __construct($id, $nom, $prenom, $ville, $sexe, $date_naissance, $image) {
        $this->id = $id;
        $this->nom = $nom;
        $this->prenom = $prenom;
        $this->ville = $ville;
        $this->sexe = $sexe;
        $this->date_naissance = $date_naissance;
        $this->image = $image;
    }

    public function getDate_naissance() {
        return $this->date_naissance;
    }

    public function getImage() {
        return $this->image;
    }

    public function setDate_naissance($date_naissance): void {
        $this->date_naissance = $date_naissance;
    }

    public function setImage($image): void {
        $this->image = $image;
    }

    public function getId() {
        return $this->id;
    }

    public function getNom() {
        return $this->nom;
    }

    public function getPrenom() {
        return $this->prenom;
    }

    public function getVille() {
        return $this->ville;
    }

    public function getSexe() {
        return $this->sexe;
    }

    public function setId($id): void {
        $this->id = $id;
    }

    public function setNom($nom): void {
        $this->nom = $nom;
    }

    public function setPrenom($prenom): void {
        $this->prenom = $prenom;
    }

    public function setVille($ville): void {
        $this->ville = $ville;
    }

    public function setSexe($sexe): void {
        $this->sexe = $sexe;
    }

    public function __toString(): string {
        return $this->nom . " " . $this->prenom;
    }
}
