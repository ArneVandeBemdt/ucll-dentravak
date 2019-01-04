import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakSandwichesOrderConfirmation extends DenTravakAbstractElement {

    connectedCallback() {
        super.connectedCallback();
        this.initEventListeners();
    }

    init(order) {
        this.order = order;
    }

    initEventListeners() {
        this.byId('show-sandwich-list').addEventListener('click', e => this.app().dispatchEvent(new Event('show-sandwich-list')));
        this.shadowRoot.querySelectorAll('button.rating').forEach(btnElement => {
            btnElement.addEventListener('click', element => {
                this.rateSandwich(btnElement.dataset.rating)
            })
        });
    }

    rateSandwich(userRating) {

        let rating = {}

        rating.userId = this.order.mobilePhoneNumber;
        rating.rateditem = this.order.sandwichId;
        rating.rating = userRating;

        fetch("http://193.191.177.8:10438/recommendation/recommend", {
            method: 'POST',
            body: JSON.stringify(rating),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(resAsJson => alert('Thanks for the rating'));
    }

    get template() {
        return `
            <style>
                .form-group {
                    margin-bottom: 2rem!important;
                }
                .dt-header {
                    display: flex;
                }
                .dt-header button {
                    margin-left: auto;
                }
                div.dt-sandwich-info {
                    margin-left: auto;
                }
            </style>
            <div class="animate">
                <div class="dt-header">
                    <h3>Welkom bij den Travak</h3>
                    <button id="show-sandwich-list" type="button" class="btn btn-primary">Nieuwe bestelling</button>
                </div>
                <h4>Bedankt!</h4>
                <p>Wij hebben je bestelling goed ontvangen en je kan je broodje komen ophalen vanaf 11u45.</p>
                <p>Tot zo dadelijk!</p>

                <h4>Beoordeel het broodje</h4>
                <button type="button" class="btn btn-primary bmd-btn-fab rating" data-rating="1">1</button>
                <button type="button" class="btn btn-primary bmd-btn-fab rating" data-rating="2">2</button>
                <button type="button" class="btn btn-primary bmd-btn-fab rating" data-rating="3">3</button>
                <button type="button" class="btn btn-primary bmd-btn-fab rating" data-rating="4">4</button>
                <button type="button" class="btn btn-primary bmd-btn-fab rating" data-rating="5">5</button>
            </div>
        `;
    }
}

customElements.define('travak-sandwiches-order-confirmation', DenTravakSandwichesOrderConfirmation);