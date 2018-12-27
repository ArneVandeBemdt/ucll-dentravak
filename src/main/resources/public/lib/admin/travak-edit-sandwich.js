import DenTravakAbstractElement from '../travak-abstract-element.js';
import '../common/travak-input-text.js';
import constructApiUrl from '../utils.js';

class DenTravakEditSandwich extends DenTravakAbstractElement {

    constructor() {
        super('travak-admin-app')
    }

    connectedCallback() {
        super.connectedCallback();
        this.initEventListeners();
    }

    initEventListeners() {
        this.byId('save-btn').addEventListener('click', e => this.saveSandwich());
        this.byId('cancel-btn').addEventListener('click', e => this.app().showSandwichList());
    }

    saveSandwich() {
        if (Object.is(this.sandwich.id, undefined)) {
            this.sandwich.name = this.byId('name').value;
            this.sandwich.ingredients = this.byId('ingredients').value;
            this.sandwich.price = this.byId('price').value;

            fetch(constructApiUrl('/sandwiches'), {
                method: 'POST',
                body: JSON.stringify(this.sandwich),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(res => res.json())
                .then(response => {
                    this.app().dispatchEvent(new CustomEvent('save-succeeded', { detail: this.sandwich }));
                    console.log('Succes: ', response)
                })
                .catch(error => console.error('Error: ', error));
        } else {
            this.sandwich.name = this.byId('name').value;
            this.sandwich.ingredients = this.byId('ingredients').value;
            this.sandwich.price = this.byId('price').value;

            fetch(constructApiUrl('sandwiches/' + this.sandwich.id), {
                method: 'PUT',
                body: JSON.stringify(this.sandwich),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(res => res.json())
                .then(response => {
                    this.app().dispatchEvent(new CustomEvent('save-succeeded', { detail: this.sandwich }));
                    console.log('Succes: ', response)
                })
                .catch(error => console.error('Error: ', error));
        }

    }

    init(sandwich) {
        if (sandwich) {
            this.sandwich = sandwich;
            this.byId('title').innerHTML = `Bewerk broodje ${sandwich.name}`;
            this.byId('name').value = sandwich.name;
            this.byId('ingredients').value = sandwich.ingredients;
            this.byId('price').value = sandwich.price;
        } else {
            this.sandwich = {};
            this.byId('title').innerHTML = `Maak een nieuw broodje`;
            this.byId('name').value = "";
            this.byId('ingredients').value = "";
            this.byId('price').value = "";
        }
    }

    get template() {
        return `
            <h4 id="title">Bewerk een broodje</h4>
            <travak-input-text id="name" label="Sandwich naam" placeholder="De naam van het broodje"></travak-input-text>
            <travak-input-text id="ingredients" label="Ingredienten" placeholder="Welke ingredienten heeft het broodje"></travak-input-text>
            <travak-input-text id="price" label="Prijs" placeholder="De prijs van het broodje"></travak-input-text>
            <button id="cancel-btn" type="button" class="btn">Cancel</button>
            <button id="save-btn" type="button" class="btn btn-primary">Save</button>
        `;
    }

}

customElements.define('travak-edit-sandwich', DenTravakEditSandwich);