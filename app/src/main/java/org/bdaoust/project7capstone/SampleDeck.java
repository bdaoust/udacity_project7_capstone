package org.bdaoust.project7capstone;

import io.magicthegathering.javasdk.resource.Card;

public class SampleDeck extends Deck{

    public SampleDeck(){
        super("Feel the Burn");

        this.setLastUpdatedTimestamp(1491006413000l);

        Card card;

        card = new Card();
        card.setName("Mountain");
        card.setMultiverseid(191401);
        card.setCmc(0.0);
        card.setType("Basic Land — Mountain");
        card.setText("null");
        card.setManaCost("null");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Magic 2010");
        card.setArtist("Sam Wood");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=191401&type=card");
        this.addCardCopies(card, 12);


        card = new Card();
        card.setName("Lightning Bolt");
        card.setMultiverseid(191089);
        card.setCmc(1.0);
        card.setType("Instant");
        card.setText("Lightning Bolt deals 3 damage to target creature or player.");
        card.setManaCost("{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Magic 2010");
        card.setArtist("Christopher Moeller");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=191089&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Urza's Mine");
        card.setMultiverseid(220948);
        card.setCmc(0.0);
        card.setType("Land — Urza’s Mine");
        card.setText("{T}: Add {C} to your mana pool. If you control an Urza's Power-Plant and an Urza's Tower, add {C}{C} to your mana pool instead.");
        card.setManaCost("null");
        card.setColorIdentity(null);
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Masters Edition IV");
        card.setArtist("Anson Maddocks");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=220948&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Urza's Power Plant");
        card.setMultiverseid(220952);
        card.setCmc(0.0);
        card.setType("Land — Urza’s Power-Plant");
        card.setText("{T}: Add {C} to your mana pool. If you control an Urza's Mine and an Urza's Tower, add {C}{C} to your mana pool instead.");
        card.setManaCost("null");
        card.setColorIdentity(null);
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Masters Edition IV");
        card.setArtist("Mark Tedin");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=220952&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Urza's Tower");
        card.setMultiverseid(220956);
        card.setCmc(0.0);
        card.setType("Land — Urza’s Tower");
        card.setText("{T}: Add {C} to your mana pool. If you control an Urza's Mine and an Urza's Power-Plant, add {C}{C}{C} to your mana pool instead.");
        card.setManaCost("null");
        card.setColorIdentity(null);
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Masters Edition IV");
        card.setArtist("Mark Poole");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=220956&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Incinerate");
        card.setMultiverseid(134751);
        card.setCmc(2.0);
        card.setType("Instant");
        card.setText("Incinerate deals 3 damage to target creature or player. A creature dealt damage this way can't be regenerated this turn.");
        card.setManaCost("{1}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Tenth Edition");
        card.setArtist("Zoltan Boros & Gabor Szikszai");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=134751&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Fireball");
        card.setMultiverseid(191076);
        card.setCmc(1.0);
        card.setType("Sorcery");
        card.setText("Fireball deals X damage divided evenly, rounded down, among any number of target creatures and/or players."
                +"\nFireball costs {1} more to cast for each target beyond the first.");
        card.setManaCost("{X}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Magic 2010");
        card.setArtist("Dave Dorman");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=191076&type=card");
        this.addCardCopies(card, 4);


        card = new Card();
        card.setName("Maze of Ith");
        card.setMultiverseid(1824);
        card.setCmc(0.0);
        card.setType("Land");
        card.setText("{T}: Untap target attacking creature. Prevent all combat damage that would be dealt to and dealt by that creature this turn.");
        card.setManaCost("null");
        card.setColorIdentity(null);
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("The Dark");
        card.setArtist("Anson Maddocks");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=1824&type=card");
        this.addCardCopies(card, 1);



        card = new Card();
        card.setName("Hammer of Bogardan");
        card.setMultiverseid(3452);
        card.setCmc(3.0);
        card.setType("Sorcery");
        card.setText("Hammer of Bogardan deals 3 damage to target creature or player."
        +"\n{2}{R}{R}{R}: Return Hammer of Bogardan from your graveyard to your hand. Activate this ability only during your upkeep.");
        card.setManaCost("{1}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Mirage");
        card.setArtist("Ron Spencer");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=3452&type=card");
        this.addCardCopies(card, 2);



        card = new Card();
        card.setName("Akoum Hellkite");
        card.setMultiverseid(401805);
        card.setCmc(6.0);
        card.setType("Creature — Dragon");
        card.setText("Flying"
                +"\nLandfall — Whenever a land enters the battlefield under your control, Akoum Hellkite deals 1 damage to target creature or player. If that land is a Mountain, Akoum Hellkite deals 2 damage to that creature or player instead.");
        card.setManaCost("{4}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("4");
        card.setToughness("4");
        card.setLoyalty(0);
        card.setSetName("Battle for Zendikar");
        card.setArtist("Jaime Jones");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=401805&type=card");
        this.addCardCopies(card, 3);


        card = new Card();
        card.setName("Arc Blade");
        card.setMultiverseid(126193);
        card.setCmc(5.0);
        card.setType("Sorcery");
        card.setText("Arc Blade deals 2 damage to target creature or player. Exile Arc Blade with three time counters on it."
                +"\nSuspend 3—{2}{R} (Rather than cast this card from your hand, you may pay {2}{R} and exile it with three time counters on it. At the beginning of your upkeep, remove a time counter. When the last is removed, cast it without paying its mana cost.)");
        card.setManaCost("{3}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Future Sight");
        card.setArtist("Shishizaru");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=126193&type=card");
        this.addCardCopies(card, 4);



        card = new Card();
        card.setName("Arc Lightning");
        card.setMultiverseid(205386);
        card.setCmc(3.0);
        card.setType("Sorcery");
        card.setText("Arc Lightning deals 3 damage divided as you choose among one, two, or three target creatures and/or players.");
        card.setManaCost("{2}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Planechase");
        card.setArtist("Andrew Goldhawk");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=205386&type=card");
        this.addCardCopies(card, 3);



        card = new Card();
        card.setName("Banefire");
        card.setMultiverseid(186613);
        card.setCmc(1.0);
        card.setType("Sorcery");
        card.setText("Banefire deals X damage to target creature or player."
                +"\nIf X is 5 or more, Banefire can't be countered by spells or abilities and the damage can't be prevented.");
        card.setManaCost("{X}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Conflux");
        card.setArtist("Raymond Swanland");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=186613&type=card");
        this.addCardCopies(card, 4);



        card = new Card();
        card.setName("Conflagrate");
        card.setMultiverseid(114909);
        card.setCmc(1.0);
        card.setType("Sorcery");
        card.setText("Conflagrate deals X damage divided as you choose among any number of target creatures and/or players."
                +"\nFlashback—{R}{R}, Discard X cards. (You may cast this card from your graveyard for its flashback cost. Then exile it.)");
        card.setManaCost("{X}{X}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Time Spiral");
        card.setArtist("Warren Mahy");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=114909&type=card");
        this.addCardCopies(card, 2);



        card = new Card();
        card.setName("Increasing Vengeance");
        card.setMultiverseid(262661);
        card.setCmc(2.0);
        card.setType("Instant");
        card.setText("Copy target instant or sorcery spell you control. If Increasing Vengeance was cast from a graveyard, copy that spell twice instead. You may choose new targets for the copies."
                +"\nFlashback {3}{R}{R} (You may cast this card from your graveyard for its flashback cost. Then exile it.)");
        card.setManaCost("{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Dark Ascension");
        card.setArtist("Anthony Francisco");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=262661&type=card");
        this.addCardCopies(card, 2);



        card = new Card();
        card.setName("Nevinyrral's Disk");
        card.setMultiverseid(420882);
        card.setCmc(4.0);
        card.setType("Artifact");
        card.setText("Nevinyrral's Disk enters the battlefield tapped."
        +"\n{1}, {T}: Destroy all artifacts, creatures, and enchantments.");
        card.setManaCost("{4}");
        card.setColorIdentity(null);
        card.setColors(null);
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(0);
        card.setSetName("Commander 2016");
        card.setArtist("Steve Argyle");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=420882&type=card");
        this.addCardCopies(card, 3);



        card = new Card();
        card.setName("Chandra, the Firebrand");
        card.setMultiverseid(259205);
        card.setCmc(4.0);
        card.setType("Planeswalker — Chandra");
        card.setText("+1: Chandra, the Firebrand deals 1 damage to target creature or player."
                +"\n−2: When you cast your next instant or sorcery spell this turn, copy that spell. You may choose new targets for the copy."
                +"\n−6: Chandra, the Firebrand deals 6 damage to each of up to six target creatures and/or players.");
        card.setManaCost("{3}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(3);
        card.setSetName("Magic 2013");
        card.setArtist("D. Alexander Gregory");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=259205&type=card");
        this.addCardCopies(card, 2);



        card = new Card();
        card.setName("Chandra Ablaze");
        card.setMultiverseid(195402);
        card.setCmc(6.0);
        card.setType("Planeswalker — Chandra");
        card.setText("+1: Discard a card. If a red card is discarded this way, Chandra Ablaze deals 4 damage to target creature or player."
                +"\n−2: Each player discards his or her hand, then draws three cards."
                +"\n−7: Cast any number of red instant and/or sorcery cards from your graveyard without paying their mana costs.");
        card.setManaCost("{4}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(5);
        card.setSetName("Zendikar");
        card.setArtist("Steve Argyle");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=195402&type=card");
        this.addCardCopies(card, 1);



        card = new Card();
        card.setName("Chandra Nalaar");
        card.setMultiverseid(393821);
        card.setCmc(5.0);
        card.setType("Planeswalker — Chandra");
        card.setText("+1: Chandra Nalaar deals 1 damage to target player."
                +"\n−X: Chandra Nalaar deals X damage to target creature."
                +"\n−8: Chandra Nalaar deals 10 damage to target player and each creature he or she controls.");
        card.setManaCost("{3}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(6);
        card.setSetName("Duel Decks Anthology, Jace vs. Chandra");
        card.setArtist("Kev Walker");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=393821&type=card");
        this.addCardCopies(card, 1);



        card = new Card();
        card.setName("Chandra, Torch of Defiance");
        card.setMultiverseid(417683);
        card.setCmc(4.0);
        card.setType("Planeswalker — Chandra");
        card.setText("+1: Exile the top card of your library. You may cast that card. If you don't, Chandra, Torch of Defiance deals 2 damage to each opponent."
                +"\n+1: Add {R}{R} to your mana pool."
                +"\n−3: Chandra, Torch of Defiance deals 4 damage to target creature."
                +"\n−7: You get an emblem with \"Whenever you cast a spell, this emblem deals 5 damage to target creature or player.\"");
        card.setManaCost("{2}{R}{R}");
        card.setColorIdentity(new String[]{"R"});
        card.setColors(new String[]{"Red"});
        card.setPower("null");
        card.setToughness("null");
        card.setLoyalty(4);
        card.setSetName("Kaladesh");
        card.setArtist("Magali Villeneuve");
        card.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=417683&type=card");
        this.addCardCopies(card, 1);
    }
}
