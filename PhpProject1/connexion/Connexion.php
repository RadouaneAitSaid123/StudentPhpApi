<?php

class Connexion {

    private $connexion;

    public function __construct() {
        $host = 'localhost';
        $dbname = 'school1';
        $login = 'root';
        $password = '';
        try {
            $this->connexion = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $login, $password);
            $this->connexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $ex) {
            die('Erreur de connexion : ' . $ex->getMessage());
        }
    }

    public function getConnexion() {
        return $this->connexion;
    }
}
