Un site dynamique d’analyse des données des bureaux de votes de Paris pour les dernières élections présidentielles et  législatives (1er et 2ème tours). 

# Spécification fonctionnelle minimale: 

Le site doit être un site public (sans authentification) permettant  aux habitants de la ville de Paris de : 

- Afficher les listes des bureaux de vote par arrondissement 

- Afficher le bureau de vote d’une personne en ayant son adresse postale (dans Paris) 

-  Faire des filtrages et statistiques 

-- Calculer le score d’un candidat (global et par arrondissement) 

-- Afficher les bureaux le rang des candidats  (un candidat donné a été premier ou dernier) 

--  Afficher l’ensemble des résultats dans un tableau et autoriser des tris sur les colonnes (Nom du candidat, Score, Arrondissement, etc..) 

D'autres fonctionnalités sont aussi possibles: 

-  Permettre au utilisateurs de s’enregistrer afin d'obtenir par mail le résultat complet (par candidat) du bureau de vote auquel il (l'utilisateur) est rattaché. 
- Permettre à un administrateur authentifié de modifier les données. 
- Etc 

Les données sur lesquelles on travailler sont disponibles sur le site de la mairie de Paris 

https://opendata.paris.fr/explore/dataset/adresse_paris/ (base de données des adresses de paris) 

https://opendata.paris.fr/explore/dataset/resultats_electoraux/?disjunctive.libelle_du_scrutin (résultats électoraux) 

https://opendata.paris.fr/explore/dataset/bureaux-de-votes/ (liste des bureaux de vote) 

https://opendata.paris.fr/explore/dataset/zones-de-rattachement-des-bureaux-de-vote-en-2014/ (zone de rattachement des electeurs)

# Comment installer un environnement de travaille sur windows 10 x64

## Installer la JDK 8
- Installer Java SE Development Kit 8u161 x64 dans l'emplacement par default
- Ajouter une nouvelle varibale d'envirenment systeme: JAVA_HOME qui point vert la jdk (exemple: C:\Program Files\Java\jdk1.8.0_161)
- Ajouter au PATH : %JAVA_HOME%\bin
si tout va bien, a present la commande "java" et "javac" devrait fonctionner dans le terminal

## Installer maven 3.5.2
- telecharger le fichier zip de maven 3.5.2
- decompresser le dans "C:\Program Files\Apache\maven\3.5.2"
- Ajouter une nouvelle varibale d'envirenment systeme: M2_HOME qui point vert maven (exemple: C:\Program Files\Apache\maven\3.5.2)
- Ajouter au PATH : %M2_HOME%\bin
si tout va bien, a present la commande "mvn" devrait fonctionner dans le terminal

## Installer IntelliJ IDEA 2017.3.4
- installer IntelliJ IDEA 2017.3.4 (version complete), creer juste le racourci x64 
- ouvrir le fichier suivant avec un editeur de text: C:\Program Files\JetBrains\IntelliJ IDEA 2017.3.4\bin\idea64.exe.vmoptions
- modifier idea64.exe.vmoptions suivant sa preference.
- lancer idea 

## Configurer IntelliJ IDEA 2017.3.4 avec la JDK
- Dans: configure -> project defaults -> project structure 
- Dans SDK: cliquer sur le plus, puis JDK
- choisir l'emplacement de la JDK
- Dans documentation path: ajouter un lien (par default)

## Configurer IntelliJ IDEA 2017.3.4 avec maven
- Dans configure -> settings -> Build, Execution, Deployment -> Build Tools -> Maven
- selection le nouveau maven
- Dans Importing
- cocher source et documentation et import maven  automatically
- desactiver kotlin plugin

Remarque:
- il faut la premiere fois: update repo of maven
- lancer intellij en mode administrateur (obligatoire pour fixer une erreur de glassfish)
- Racourcis de documentation: Shift+F1 or Ctrl+q

## Installer GlassFish 5.0.0

## Installer MySql 5.7.21
