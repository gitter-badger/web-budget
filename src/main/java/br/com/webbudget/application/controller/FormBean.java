/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.NavigationManager;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.LazyDataProvider;
import br.com.webbudget.application.components.table.LazyFilter;
import br.com.webbudget.application.components.table.LazyModel;
import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.Parameter.of;
import br.com.webbudget.domain.entities.PersistentEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

/**
 * The abstract form controller, this class hold all the commom functionalities
 * that a single form will have.
 *
 * This class already implement the lazy loading support for primefaces with
 * the implementation of the {@link LazyDataProvider}
 *
 * @param <T> the type to be manipulated with this controller, needs to be a
 * class of the domain model child of the {@link PersistentEntity}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/03/2018
 */
public abstract class FormBean<T extends PersistentEntity> extends AbstractBean
        implements LazyDataProvider<T> {

    @Getter
    @Setter
    protected T value;

    @Getter
    protected List<T> data;

    @Getter
    protected ViewState viewState;

    @Getter
    protected final LazyFilter filter;
    @Getter
    protected final LazyDataModel<T> dataModel;

    protected final NavigationManager navigation;

    /**
     * Create the bean and initialize the default data
     */
    public FormBean() {

        this.dataModel = new LazyModel<>(this);

        this.filter = LazyFilter.getInstance();
        this.navigation = NavigationManager.getInstance();

        this.initializeNavigationManager();
    }

    /**
     * This method should be used to initialize the {@link NavigationManager} for this controller
     */
    protected abstract void initializeNavigationManager();

    /**
     * Use this to initialize the basic attributes of the form model
     *
     * @param id the id of the entity to search on the database or leave null to initialize a new instance of the model
     * @param viewState the actual view state to initialize the view
     */
    public abstract void initialize(long id, ViewState viewState);

    /**
     * Perform a save operation
     */
    public abstract void doSave();

    /**
     * Perform a update operation
     */
    public abstract void doUpdate();

    /**
     * Perform a delete operation
     *
     * After delete, this method needs to return the navigation case to the after delete action page
     *
     * @return the page to redirect the user after de delete operation
     */
    public abstract String doDelete();

    /**
     * This is the default initialization method, if you want more logic here just override this.
     *
     * By default, this method initialize the view in listing mode by setting the {@link ViewState} to LISTING value
     */
    public void initialize() {
        this.viewState = ViewState.LISTING;
    }

    /**
     * Update the default listing component, by default this component is named by "itemsListing"
     */
    public void updateListing() {
        this.updateComponent("itemsListing");
    }

    /**
     * Clear the form filters
     */
    public void clearFilters() {
        this.filter.clear();
    }

    /**
     * Redirect the user to the listing page defined in the {@link NavigationManager}
     *
     * @return the listing page
     */
    public String changeToListing() {
        return this.navigation.to(LIST_PAGE);
    }

    /**
     * Redirect the user to the add page defined in the {@link NavigationManager}
     *
     * @return the add page
     */
    public String changeToAdd() {
        return this.navigation.to(ADD_PAGE);
    }

    /**
     * Redirect the user to the edit page defined in the {@link NavigationManager}
     *
     * @return the edit page
     */
    public String changeToEdit(long id) {
        return this.navigation.to(UPDATE_PAGE, of("id", id));
    }

    /**
     * Redirect the user to the detail page defined in the {@link NavigationManager}
     */
    public void changeToDetail() {
        this.navigation.redirect(DETAIL_PAGE, of("id", this.value.getId()));
    }

    /**
     * Redirect the user to the delete page defined in the {@link NavigationManager}
     *
     * @return the delete page
     */
    public String changeToDelete(long id) {
        return this.navigation.to(DELETE_PAGE, of("id", id));
    }
}
