package com.example.application.views.GameView;

import com.example.application.data.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GameCard extends VerticalLayout {

    private final Div cardFlip;
    private final VerticalLayout mainLayout;
    private final VerticalLayout backLayout;

    public boolean isFlipped() {
        return flipped;
    }

    private boolean flipped = false;

    public GameCard() {
        setSizeFull();
        cardFlip = new Div();
        mainLayout = new VerticalLayout();
        backLayout = new VerticalLayout();

        setStyle();

        cardFlip.add(mainLayout, backLayout);
        add(cardFlip);
    }

    // Об'єднаний метод для анімації переходу з новим завданням
    public void transitionToNewTask(Task newTask, boolean wasSuccess) {
        String colorVar = wasSuccess ? "--lumo-success-color" : "--lumo-error-color";
        String contrastVar = wasSuccess ? "--lumo-success-contrast-color" : "--lumo-error-contrast-color";

        cardFlip.getElement().executeJs(
                """
                const el = this;
                const front = el.querySelector('.card-front');
                
                // Етап 1: Зміна кольору та приховування
                front.style.backgroundColor = getComputedStyle(document.documentElement).getPropertyValue($0);
                front.style.color = getComputedStyle(document.documentElement).getPropertyValue($1);
                
                el.style.transition = 'transform 0.4s ease-in';
                el.style.transform = 'translateX(-150vw)';
                
                // Етап 2: Після завершення анімації приховування
                el.addEventListener('transitionend', function hideHandler(event) {
                    if (event.propertyName === 'transform') {
                        el.removeEventListener('transitionend', hideHandler);
                        
                        // Повідомляємо Java про готовність до оновлення контенту
                        el.dispatchEvent(new CustomEvent('cardHidden'));
                        
                        // Етап 3: Підготовка до появи
                        setTimeout(() => {
                            front.style.backgroundColor = 'orange';
                            front.style.color = 'white';
                            
                            el.style.transition = 'none';
                            el.style.transform = 'translateX(-150vw)';
                            
                            // Форсуємо перерахунок
                            void el.offsetHeight;
                            
                            // Етап 4: Анімація появи
                            setTimeout(() => {
                                el.style.transition = 'transform 0.4s ease-out';
                                el.style.transform = 'translateX(0)';
                            }, 50);
                            
                        }, 100);
                    }
                });
                """,
                colorVar, contrastVar
        );

        // Слухаємо подію про готовність до оновлення контенту
        cardFlip.getElement().addEventListener("cardHidden", e -> {
            setTask(newTask);
        });
    }

    // Спрощений метод для першої появи
    public void appearNormal() {
        cardFlip.getElement().executeJs(
                """
                const el = this;
                const front = el.querySelector('.card-front');
                front.style.backgroundColor = 'orange';
                front.style.color = 'white';
        
                el.style.transition = 'none';
                el.style.transform = 'translateX(-150vw)';
        
                requestAnimationFrame(() => {
                    el.style.transition = 'transform 0.4s ease-out';
                    el.style.transform = 'translateX(0)';
                });
                """
        );
    }

    // Старий метод hideCard залишається для сумісності
    public void hideCard(boolean success) {
        String colorVar = success ? "--lumo-success-color" : "--lumo-error-color";
        String contrastVar = success ? "--lumo-success-contrast-color" : "--lumo-error-contrast-color";

        cardFlip.getElement().executeJs(
                """
                const el = this;
                const front = el.querySelector('.card-front');
        
                front.style.backgroundColor = getComputedStyle(document.documentElement).getPropertyValue($0);
                front.style.color = getComputedStyle(document.documentElement).getPropertyValue($1);
        
                el.style.transition = 'none';
                el.style.transform = 'translateX(0)';
        
                void el.offsetHeight;
        
                el.style.transition = 'transform 0.4s ease-in';
                el.style.transform = 'translateX(-150vw)';
        
                el.addEventListener('transitionend', function handler(event) {
                    if (event.propertyName === 'transform') {
                        el.removeEventListener('transitionend', handler);
                        console.log('Анімація завершена');
                    }
                });
                """,
                colorVar, contrastVar
        );
    }

    public void flipCard() {
        if (flipped) {
            cardFlip.getStyle().set("transform", "rotateY(0deg)");
            flipped = false;
        } else {
            cardFlip.getStyle().set("transform", "rotateY(180deg)");
            flipped = true;
        }
    }
    public void setTask(Task task) {
        Card card = task.getCard();
        GameMode mode = task.getGameMode();
        mainLayout.removeAll();
        backLayout.removeAll();
        mainLayout.setAlignItems(Alignment.CENTER);
        backLayout.setAlignItems(Alignment.CENTER);

        // Front side content (mainLayout)
        Div frontText = new Div();
        if (mode == GameMode.REVISION) {
            frontText.setText(card.getTranslate());
        } else if (mode == GameMode.DEFINITIONS) {
            frontText.setText(card.getDefinition());
        }
        setupTextDiv(frontText);
        mainLayout.add(frontText);

        // Back side content (backLayout)
        if (card.getImage() != null && !card.getImage().isEmpty()) {
            Image image = new Image(card.getImage(), "Card Image");
            image.setMaxHeight("60%");
            image.setMaxWidth("100%");
            image.getStyle()
                    .set("object-fit", "contain")
                    .set("margin-bottom", "10px")
                    .set("flex-shrink", "0");
            image.getElement().addEventListener("error", e -> image.setVisible(false));
            backLayout.add(image);
        }

        Div backWord = new Div();
        backWord.setText(card.getWord());
        setupTextDiv(backWord);
        backLayout.add(backWord);
    }

    private void setupTextDiv(Div textDiv) {
        textDiv.getStyle()
                .set("color", "white")
                .set("text-align", "center")
                .set("font-weight", "bold")
                .set("width", "100%")
                .set("height", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("box-sizing", "border-box");
        textDiv.getElement().getStyle()
                .set("word-wrap", "break-word")
                .set("word-break", "break-word")
                .set("hyphens", "auto")
                .set("white-space", "normal")
                .set("overflow-wrap", "break-word")
                .set("line-height", "1.2")
                .set("font-size", "clamp(0.8rem, 4vw, 2rem)")
                .set("padding", "10px");
        textDiv.getElement().executeJs(
                "const element = this;" +
                        "const container = element.parentElement;" +
                        "function adjustFontSize() {" +
                        "  let fontSize = parseInt(window.getComputedStyle(element).fontSize);" +
                        "  const maxFontSize = Math.min(container.clientWidth * 0.08, 32);" +
                        "  const minFontSize = 12;" +
                        "  fontSize = Math.min(fontSize, maxFontSize);" +
                        "  element.style.fontSize = fontSize + 'px';" +
                        "  while ((element.scrollHeight > container.clientHeight || element.scrollWidth > container.clientWidth) && fontSize > minFontSize) {" +
                        "    fontSize -= 1;" +
                        "    element.style.fontSize = fontSize + 'px';" +
                        "  }" +
                        "}" +
                        "setTimeout(() => { adjustFontSize(); }, 100);" +
                        "window.addEventListener('resize', adjustFontSize);"
        );
    }

    private void setStyle() {
        // Стилі для GameCard контейнера (this)
        getStyle().set("perspective", "1000px");

        // cardFlip стилі
        cardFlip.addClassName("card-flip");
        cardFlip.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("position", "relative")
                .set("transform-style", "preserve-3d")
                .set("transition", "transform 0.6s ease");

        // mainLayout (фронтальна сторона)
        mainLayout.addClassName("card-front");
        mainLayout.setSizeFull();
        mainLayout.getStyle()
                .set("position", "absolute")
                .set("width", "100%")
                .set("height", "100%")
                .set("backface-visibility", "hidden")
                .set("background-color", "orange")
                .set("border-radius", "10px")
                .set("overflow", "hidden")
                .set("color", "white");

        // backLayout (зворотня сторона)
        backLayout.addClassName("card-back");
        backLayout.setSizeFull();
        backLayout.getStyle()
                .set("position", "absolute")
                .set("width", "100%")
                .set("height", "100%")
                .set("backface-visibility", "hidden")
                .set("background-color", "darkorange")
                .set("border-radius", "10px")
                .set("overflow", "hidden")
                .set("color", "white")
                .set("transform", "rotateY(180deg)");
    }
}