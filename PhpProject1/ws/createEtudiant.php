<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';
    create();
}

function create() {

    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $nom = $_POST['nom'];
        $prenom = $_POST['prenom'];
        $ville = $_POST['ville'];
        $sexe = $_POST['sexe'];
        $date_naissance = $_POST['date_naissance'];
        $image = $_POST['image'];
        $target_path = "Images/";
        $imageStore = rand() . "_" . time() . ".jpeg";
        $target_path = $target_path . "/" . $imageStore;
        file_put_contents($target_path, base64_decode($image));
        $es = new EtudiantService();
        $es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe, $date_naissance, $imageStore));
    }
}
?>