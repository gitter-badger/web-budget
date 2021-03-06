/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.entities;

import java.io.Serializable;

/**
 * The basic entity definition for all the classes in the application
 *
 * @param <T> the type of
 *
 * @author Arthur Gregorio
 *
 * @version 4.0.0
 * @since 1.0.0, 06/10/2013
 */
public interface IPersistentEntity<T extends Serializable> {

    /**
     * @return the ID of the entity
     */
    T getId();

    /**
     * @return if the entity is saved or not
     */
    boolean isSaved();

    /**
     * helper method to call validation on the entity
     */
    void validate();

    /**
     * @return ther inverse of {@link #isSaved()}
     */
    default boolean isNotSaved() {
        return !this.isSaved();
    }
}
