package com.movie.streaming.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity
 * 

 *
 */
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary key
	 */
	@Id
	@Column(name = "ID")
	private String id;
	
	/**
	 * Category
	 */
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;
	
	/**
	 * List of movies
	 */
	@JsonManagedReference
	@OneToMany(mappedBy = "CATEGORY", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<MovieEntity> movies = new ArrayList<MovieEntity>();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the movies
	 */
	public List<MovieEntity> getMovies() {
		return movies;
	}

	/**
	 * @param movies the movies to set
	 */
	public void setMovies(List<MovieEntity> movies) {
		this.movies = movies;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
