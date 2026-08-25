// Grab every .slide element inside the slideshow container.
// querySelectorAll returns ALL matches (unlike querySelector,
// which only grabs the first one) — here that's all 4 slides.
const slides = document.querySelectorAll("#about-slideshow .slide");

// Tracks which slide is currently showing, by its position
// in the list (0 = first slide, 1 = second, etc.)
let currentSlide = 0;

const SLIDE_DURATION_MS = 3000; // 3000 milliseconds = 3 seconds
                                 // between each slide change

function showNextSlide() {
    if (slides.length === 0) {
        return;
    }

    // Step 1: hide the slide that's currently showing
    slides[currentSlide].classList.remove("active");

    // Step 2: move to the next index.
    // The % (modulo) operator is what makes this LOOP back to
    // the start instead of running off the end of the list.
    // Example with 4 slides (indices 0,1,2,3):
    //   currentSlide = 0 → (0 + 1) % 4 = 1
    //   currentSlide = 1 → (1 + 1) % 4 = 2
    //   currentSlide = 2 → (2 + 1) % 4 = 3
    //   currentSlide = 3 → (3 + 1) % 4 = 0   ← wraps back to start!
    currentSlide = (currentSlide + 1) % slides.length;

    // Step 3: show the new current slide
    slides[currentSlide].classList.add("active");
}

// setInterval runs a function repeatedly, forever, waiting the
// given number of milliseconds between each run. This is what
// makes the slideshow "automatic" — no click needed.
if (slides.length > 1) {
    setInterval(showNextSlide, SLIDE_DURATION_MS);
}
