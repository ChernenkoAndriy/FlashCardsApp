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

    public boolean isFlippable() {
        return isFlippable;
    }

    public void setFlippable(boolean flippable) {
        isFlippable = flippable;
    }

    private boolean isFlippable = true;
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
    // Оновлений метод transitionToNewTask у GameCard.java

    // У GameCard.java змініть метод transitionToNewTask:

    public void transitionToNewTask(Task newTask, boolean wasSuccess, Runnable onFinishCallback) {
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
                            el.style.transform = 'translateX(150vw)';
                            
                            // Форсуємо перерахунок
                            void el.offsetHeight;
                            
                            // Етап 4: Анімація появи
                            setTimeout(() => {
                                el.style.transition = 'transform 0.4s ease-out';
                                el.style.transform = 'translateX(0)';
                                
                                // Повідомляємо про завершення появи
                                el.addEventListener('transitionend', function showHandler(event) {
                                    if (event.propertyName === 'transform') {
                                        el.removeEventListener('transitionend', showHandler);
                                        el.dispatchEvent(new CustomEvent('cardShown'));
                                    }
                                });
                            }, 50);
                            
                        }, 100);             
                    }
                });
                """,
                colorVar, contrastVar
        );

        // Слухач для оновлення контенту картки
        cardFlip.getElement().addEventListener("cardHidden", e -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                setTask(newTask);
            }));
        });

        // Слухач для виконання callback після завершення анімації
        cardFlip.getElement().addEventListener("cardShown", e -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                if (onFinishCallback != null) {
                    onFinishCallback.run();
                }
            }));
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
    public void flipCard() {
        if (!isFlippable) return;
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

        Div frontText = new Div();
        if (mode == GameMode.REVISION) {
            frontText.setText(card.getTranslate());
            setFlippable(true);
        } else if (mode == GameMode.DEFINITIONS) {
            frontText.setText(card.getDefinition());
            setFlippable(true);
        } else if (mode == GameMode.SENTENCECREATOR) {
            frontText.setText(card.getWord());
            if(flipped){
                flipCard();
            }
            setFlippable(false);
        }
        setupTextDiv(frontText);
        mainLayout.add(frontText);

        if (card.getImage() != null) {
            Image image = new Image(
                    "data:" + card.getImageType() + ";base64," +
                            java.util.Base64.getEncoder().encodeToString(card.getImage()),
                    "Card Image"
            );
            image.setMaxHeight("60%");
            image.setMaxWidth("100%");
            image.getStyle()
                    .set("object-fit", "contain")
                    .set("margin-bottom", "10px")
                    .set("flex-shrink", "0")
                    .set("border-radius", "8px");
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
        getStyle().set("perspective", "1000px");

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
    public void setTask(Task task, String sentence) {
        Card card = task.getCard();
        mainLayout.removeAll();
        backLayout.removeAll();
        mainLayout.setAlignItems(Alignment.CENTER);
        backLayout.setAlignItems(Alignment.CENTER);

        Div frontText = new Div();
            frontText.setText(sentence);
            setFlippable(true);
        setupTextDiv(frontText);
        mainLayout.add(frontText);

        if (card.getImage() != null) {
            Image image = new Image(
                    "data:" + card.getImageType() + ";base64," +
                            java.util.Base64.getEncoder().encodeToString(card.getImage()),
                    "Card Image"
            );
            image.setMaxHeight("60%");
            image.setMaxWidth("100%");
            image.getStyle()
                    .set("object-fit", "contain")
                    .set("margin-bottom", "10px")
                    .set("flex-shrink", "0")
                    .set("border-radius", "8px");
            backLayout.add(image);
        }

        Div backWord = new Div();
        backWord.setText(card.getWord());
        setupTextDiv(backWord);
        backLayout.add(backWord);
    }
}