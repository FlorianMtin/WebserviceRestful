package service;

import java.util.ArrayList;
import java.util.List;


import javax.ws.rs.*;
import com.google.gson.Gson;


import meserreurs.MonException;
import metier.*;
import persistance.DialogueBd;

@Path("/mediatheque")
public class WService {


	/***************************************************/
	/***************Partie sur les adh�rents **************/
	/*****************************************************/
	@POST
	@Path("/Adherents/ajout/{unAdh}")
	@Consumes("application/json")
	public void insertionAdherent(String unAdherent) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Adherent unAdh = gson.fromJson(unAdherent, Adherent.class);
		try {
			String mysql = "";
			mysql = "INSERT INTO adherent (nom_adherent, prenom_adherent, ville_adherent) ";
			mysql += " VALUES ( \'" + unAdh.getNomAdherent() + "\', \'" + unAdh.getPrenomAdherent();
			mysql += "  \', \'" + unAdh.getVilleAdherent() + "\') ";
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}
			/** SUPPRESION OEUVRE **/
	@DELETE
	@Path("/Oeuvres/supprimerOeuvre/{Id}")
	@Consumes("application/json")
	public void supprimerOeuvreId(@PathParam("Id") String idOeuvre) throws Exception {
		String mysql = "";

		try {
			mysql = "DELETE";
			mysql += " FROM oeuvrevente WHERE id_oeuvrevente = " + idOeuvre + ";";
			System.out.println(mysql);
			supprimerOeuvre(mysql);
		} catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}

	private void supprimerOeuvre(String requete) {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		try {
			unDialogueBd.insertionBD(requete);
		} catch (MonException e) {
			e.printStackTrace();
		}
	}
                 /** Modification OEUVRE **/
	@PUT
	@Path("/Oeuvres/Modifier/{uneOeuv}")
	@Consumes("application/json")
	public void modificationOeuvreId(String uneOeuvre) throws MonException{
		String mysql="";
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Oeuvrevente uneOeuv = gson.fromJson(uneOeuvre, Oeuvrevente.class);
		try {
			mysql = "UPDATE Oeuvrevente set titre_oeuvrevente = '" +uneOeuv.getTitreOeuvrevente() +
					"' , etat_oeuvrevente = '" + uneOeuv.getEtatOeuvrevente() +
					"' , prix_oeuvrevente = '" + uneOeuv.getPrixOeuvrevente() +
					"' WHERE id_oeuvrevente = '" + uneOeuv.getIdOeuvrevente() +"'";
			System.out.println(mysql);

			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}

	@GET
	@Path("/Adherents")
	@Produces("application/json")
	public String rechercheLesAdherents() throws MonException {
		List<Object> rs;
		List<Adherent> mesAdherents = new ArrayList<Adherent>();
		int index = 0;
		try {
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			String mysql = "";

			mysql = "SELECT * FROM adherent ORDER BY id_adherent ASC";

			rs = unDialogueBd.lecture(mysql);

			while (index < rs.size()) {

				// On cr�e un objet Adherent
				Adherent unAdh = new Adherent();
				unAdh.setIdAdherent(Integer.parseInt(rs.get(index + 0).toString()));
				unAdh.setNomAdherent(rs.get(index + 1).toString());
				unAdh.setPrenomAdherent(rs.get(index + 2).toString());
				unAdh.setVilleAdherent(rs.get(index + 3).toString());
				index = index + 4;

				mesAdherents.add(unAdh);
			}		
						
			Gson gson = new Gson();
			String json = gson.toJson(mesAdherents);
			return json;
		
		} catch (MonException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	
	/***************************************************/
	/***************Partie sur les oeuvres  **************/
	/*****************************************************/



	@POST
	@Path("/Oeuvres/ajout/{newoeuvre}")
	@Consumes("application/json")
	public void insertionOeuvre(String oeuvre) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Oeuvre newoeuvre = gson.fromJson(oeuvre, Oeuvre.class);
		try {
			String mysql = "";
			mysql = "INSERT INTO oeuvrevente (id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente, prix_oeuvrevente, id_proprietaire) ";
			mysql += " VALUES ( \'" + newoeuvre.getIdentifiant()+ "\', \'" + newoeuvre.getTitre()+  "\', \'"  + newoeuvre.getEtat() +  "\', \'"  + newoeuvre.getPrix()+  "\', \'"  + newoeuvre.getPrix()+  "\') ";
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}
	
	@GET
	@Path("/Oeuvres/{Id}")
	@Produces("application/json")
	public String rechercherOeuvreId(@PathParam("Id")  String idOeuvre) throws MonException, Exception
	{

		String mysql = "";
		String json ="";
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " FROM Oeuvrevente WHERE id_Oeuvrevente = " + idOeuvre + ";";
			uneOeuvre = rechercherOeuvre(mysql);
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);
			
		} catch (MonException e)
		{
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}

	@GET
	@Path("/Oeuvres/lib/{libelle}")
	@Produces("application/json")
	public String rechercheOeuvreLibelle(@PathParam("libelle") String lib) throws MonException, Exception
	{
		String json="";
		String  mysql;
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "select id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " from Oeuvrevente where titre_oeuvrevente = " + lib+ ";";
			uneOeuvre = rechercherOeuvre(mysql);
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);
			
		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}


	// recherche d'une Oeuvre
	// On factorise la requ�te qui doit rendre une oeuvre en vente
	public Oeuvrevente rechercherOeuvre(String requete) throws MonException
	{
		
		List<Object> rs;
		Oeuvrevente uneOeuvre=null;
		try
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {
			
				uneOeuvre = new Oeuvrevente();
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get(0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get(1).toString());
				uneOeuvre.setEtatOeuvrevente(rs.get(2).toString());
				uneOeuvre.setPrixOeuvrevente(Float.parseFloat(rs.get(3).toString()));
				int id = Integer.parseInt(rs.get(4).toString());
				// On appelle la recherche d'un propri�taire
				uneOeuvre.setProprietaire(rechercherProprietaire(id));
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return uneOeuvre;
		
	}
	
	//****************************
	// Recherche d'un pro^pri�taire 
	//****************************
	
	public Proprietaire rechercherProprietaire(int  id) throws MonException
	{
		
		List<Object> rs;
		Proprietaire  unProprietaire=null;
		String requete = " select * from Proprietaire where id_Proprietaire =" + id + ";";
		try 
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {
			
				unProprietaire = new Proprietaire();
				
				unProprietaire.setIdProprietaire(Integer.parseInt(rs.get(0).toString()));
				unProprietaire.setNomProprietaire(rs.get(1).toString());
				unProprietaire.setPrenomProprietaire(rs.get(2).toString());
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return unProprietaire;
	}	
	
	//****************************
	// Liste des oeuvres 

	//****************************
	

	@GET
	@Path("/Oeuvres")
	// r�cup�re la valeur pass�� par webResource.path("hello").path("xxxx")
	@Produces("application/json")
	public  String  consulterListeOeuvre() throws MonException {
		List<Object> rs;
		List<Oeuvrevente> mesOeuvres = new ArrayList<Oeuvrevente>();
		String mysql="";
		
		int index = 0;
		try {
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente";
			mysql += " FROM Oeuvrevente order by titre_oeuvrevente ;";
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs =unDialogueBd.lecture(mysql);
			while (index < rs.size()) {
				// On cr�e un stage
				Oeuvrevente uneOeuvre = new Oeuvrevente();
				// il faut redecouper la liste pour retrouver les lignes
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get( index + 0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get( index + 1 ).toString());
				// On incr�mente tous les 2 champs
				index = index + 2;
				mesOeuvres.add(uneOeuvre);
			}
			Gson gson = new Gson();
			String json = gson.toJson(mesOeuvres);
			return json;
		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}
	
	
}
