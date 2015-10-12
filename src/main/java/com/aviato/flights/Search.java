/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aviato.flights;


public class Search {

	private String origin;

	private String destination;

	private String departDate;

	private String returnDate;

	private int adults;

	private int children;

	private int seniors;

	private int infants;

	private int lapInfants;

	private Cabin cabin;

	public Search() {
		// default constructor
	}

	public Search(String origin, String destination, String departDate, String returnDate, int adults, int children, int seniors, int infants, int lapInfants, Cabin cabin) {
		this.origin = origin;
		this.destination = destination;
		this.departDate = departDate;
		this.returnDate = returnDate;
		this.adults = adults;
		this.children = children;
		this.seniors = seniors;
		this.infants = infants;
		this.lapInfants = lapInfants;
		this.cabin = cabin;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartDate() {
		return departDate;
	}

	public void setDepartDate(String departDate) {
		this.departDate = departDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public int getAdults() {
		return adults;
	}

	public void setAdults(int adults) {
		this.adults = adults;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public int getSeniors() {
		return seniors;
	}

	public void setSeniors(int seniors) {
		this.seniors = seniors;
	}

	public int getInfants() {
		return infants;
	}

	public void setInfants(int infants) {
		this.infants = infants;
	}

	public int getLapInfants() {
		return lapInfants;
	}

	public void setLapInfants(int lapInfants) {
		this.lapInfants = lapInfants;
	}

	public Cabin getCabin() {
		return cabin;
	}

	public void setCabin(Cabin cabin) {
		this.cabin = cabin;
	}

}
