document.addEventListener('DOMContentLoaded', () => {
    const seatButtons = document.querySelectorAll('.seat-btn');
    const selectedListEl = document.getElementById('selected-seats-list');
    const totalPriceEl = document.getElementById('total-price-display');
    const checkoutBtn = document.getElementById('checkout-btn');
    const hiddenInputsContainer = document.getElementById('hidden-seat-inputs');

    let selectedSeats = [];

    seatButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const status = btn.getAttribute('data-status');
            if (status !== 'FREE') {
                return;
            }

            const seatId = btn.getAttribute('data-id');
            const row = btn.getAttribute('data-row');
            const seatNum = btn.getAttribute('data-num');
            const sectorName = btn.getAttribute('data-sector-name');
            const price = parseFloat(btn.getAttribute('data-price'));

            const existingIndex = selectedSeats.findIndex(s => s.id === seatId);

            if (existingIndex > -1) {
                // Deselect
                selectedSeats.splice(existingIndex, 1);
                btn.classList.remove('seat-SELECTED');
            } else {
                // Select
                selectedSeats.push({
                    id: seatId,
                    row: row,
                    seatNum: seatNum,
                    sectorName: sectorName,
                    price: price
                });
                btn.classList.add('seat-SELECTED');
            }

            updateUI();
        });
    });

    function updateUI() {
        if (!selectedListEl || !totalPriceEl || !checkoutBtn || !hiddenInputsContainer) return;

        // Clear current UI list
        selectedListEl.innerHTML = '';
        hiddenInputsContainer.innerHTML = '';

        if (selectedSeats.length === 0) {
            selectedListEl.innerHTML = '<p class="text-muted" style="color: #94a3b8; font-size: 0.9rem;">Nincs még kiválasztott ülőhely. Kattintson a zöld szabad székekre!</p>';
            totalPriceEl.textContent = '0 Ft';
            checkoutBtn.disabled = true;
            checkoutBtn.style.opacity = '0.5';
            checkoutBtn.style.cursor = 'not-allowed';
            return;
        }

        let total = 0;

        selectedSeats.forEach(seat => {
            total += seat.price;

            // Add summary item
            const item = document.createElement('div');
            item.className = 'selected-seat-item';
            item.innerHTML = `
                <div>
                    <strong>${seat.sectorName}</strong><br>
                    <small style="color: #64748b;">${seat.row}. sor ${seat.seatNum}. szék</small>
                </div>
                <div style="font-weight: 600; color: #831843;">
                    ${seat.price.toLocaleString('hu-HU')} Ft
                </div>
            `;
            selectedListEl.appendChild(item);

            // Add hidden form input
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'selectedSeatIds';
            input.value = seat.id;
            hiddenInputsContainer.appendChild(input);
        });

        totalPriceEl.textContent = total.toLocaleString('hu-HU') + ' Ft';
        checkoutBtn.disabled = false;
        checkoutBtn.style.opacity = '1';
        checkoutBtn.style.cursor = 'pointer';
    }

    // Initial render
    updateUI();
});
