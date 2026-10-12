package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.*
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceKey

internal object ResearchBookContent {
  private val contentByEntry = mapOf(
    ResearchEntries.Thavma.THAVMA to listOf(
      title("Thavma"),
      paragraph(
        """
          I was merely toying with that wand -if it can even be called that- when this tome
          flew into my hands! I can sense great power within it.
        """
      ),
      paragraph(
        """
          The cover reads "Elements", but a lot of its pages appear blank, sealed by some magic.
        """
      ),
      paragraph(
        """
          To read them, I will first need to break that seal. It won't be easy... but
          I have a feeling it will be worth my efforts.
        """
      ),
      pageBreak(),
      paragraph(
        """
          I will document all my findings inside the book, so that I can recall them later.
        """
      ),
    ),
    ResearchEntries.Thavma.ARCANE_LENS to listOf(
      title("The Arcane Lens"),
      paragraph(
        """
          The part of the book I can read describes an arcane tool that "allows the user
          to see", whatever that might mean. I have a feeling that crafting it could assist
          my work in unsealing the other pages.
        """
      ),
      paragraph(
        """
          The blueprint describes a hexagonal device, much like a prism,
          made with those colorful crystals I found lying in a cave.
        """
      ),
      paragraph(
        """
          I should look at the world through its lens, maybe it will uncover something useful.
        """
      ),
    ),
    ResearchEntries.Thavma.INFUSION to listOf(
      figure(
        Texture("gui/images/infusion", 1916, 1036, 1916, 1036),
        180,
        101,
        "An image of the infusion altar",
      ),
    ),
    ResearchEntries.Lore.SEA_MYTH to listOf(
      title("From the Heart's Eclipsed Depths"),
      paragraph(
        $$"""
        Once the symposium partakers have each had their fill of wine and nestle in their seats of the andron, one of them crieth: “O %1$s, day and night do we ponder the quintessential mysteries of the world, pay thorough mind to man’s doom and mind no scrutiny of our own judgement, yet the unhiddenness most simple eludeth us. What is, in truth, love? In waking and dreaming we see the truth of love all around and follow the path of love unto true knowledge. Thus, why may we not capture the essence of the thing, or more-than-thing?”
      """, Style.EMPTY.withItalic(true), "didaskale" to Style.EMPTY
      ),
      paragraph(
        """
        The master casteth his gaze over the sea into the distance. He answereth that love is a thing most simple indeed, though her faces hardly can enumerate he who sifteth gold out of sand grain by grain.
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        “But is there no saving grace for him who seeketh sense therein? Dost thou know of a fable that in the blink of an eye illuminateth the shadow of doubt?” asketh another.
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        The sage confesseth that such might outnumber the night velvet’s pearls, and still one resoundeth stubbornly in his mind’s ear.
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        "“Tarry no longer, speak!”", Style.EMPTY.withItalic(true)
      ),
      title(""),
      paragraph(
        """
        Longer than long ago, when great heroes walked this earth and one’s hands were of no use to one’s fate, there was a city upon the seashore. Everyone there could get what they desired if they were able to accept what they did not. Just as the light chariot set out on its journey across the sky in the morning, it woke up the bustle of street barterers, hurried up soldiers onto the training grounds, opened wide the gate of the temple and shooed sneaks back into sheep’s clothing. 
      """
      ),
      paragraph(
        """
        Despite the merchants’ entrepreneurship and, plainly put, measuredly obliging greed, trading ships stayed clear of the city. It was because close by, a terrible monster lived in the sea which rose from the water with painful regularity and with its uncountable heads it ravaged the city bloodthirstily. Dearly did the city’s armed forces pay for the fact that the monster was invincible. Only the sharpest blade could pierce its skin, but even if one head was chopped off, the remaining ones doubled.
      """
      ),
      paragraph(
        """
        The monster, however, seemed to accept offerings with gratitude, since for every life extinguished underwater, tens were spared on land. It would even return the cauldron, as if encouraging the people to continue their efforts to its great amusement. Thus, the whole city waited, entranced by the everyday, for someone to solve the monster’s riddles, banishing it.
      """
      ),
      paragraph(
        """
        And so, the day once again arrived when one unfortunate individual would either have to satisfy the voraciousness with which time crushes rocks, or chisel themselves eternally into the rock-hard tablets of history.
      """
      ),
      paragraph(
        """
        A young hero, who had already earned this title, had just returned home from his travels– he caressed the bare ears of king Midas, recorded the songs of the sirens, buried Ikaros’ body and today he intended on planting a pomegranate from the underground lands in front of his house. He stepped over the threshold and into his arms fell his mother. He cleared her auburn hair off his face and greeted her surprisedly: “Mother, how are you still here? Has danger clouded your wits? Although… I’m glad to see you again.”
      """
      ),
      paragraph(
        """
        “And I you, my son! My worries now gone nearly surpassed my joy in seeing you safe and sound. Please tell me you are putting an end to those adventures and staying here,” she responded and wiped her eyes.
      """
      ),
      paragraph(
        """
        The hero could not contain a bitter smile: “I don’t know how to say this, mother, but I’m afraid I’m truly staying here forever,” he admitted and guiltily observed his mother’s puzzled expression. “Today is Hopeful Day, as the monster again claims its bloody tribute, mother. I want to face it and try to save my homeland.”
      """
      ),
      paragraph(
        """
        Happy tears on her cheek grew heavy like a soul does with deceit. “For the love of all that is holy, have you not risked your youth more than enough? Do not cast away your life so carelessly! Even the brightest scholars from our city were swallowed by the depths. We do not even know how many riddles each of them guessed correctly, let alone a single one. Come, let us leave here and sail across the sea to the end of the world where none shall ever find us. I am old, but I have strength aplenty yet.”
      """
      ),
      paragraph(
        """
        “Then show it and let me go. My fate calls, mother, it’s only fitting to serve what I believe with all I have. I’ve survived many dangers at sea already, but what use is it when I let the ground beneath my feet bleed out into it?”
      """
      ),
      paragraph(
        """
        “Your father too was all muscle, sweat and implacable stubbornness.” She took a deep breath in. “You remind me of him so. Though to what end did this serve him, when but a memory he left of himself for good long ago, and how would I alone conduct myself without you in the wide world? Who shall soothe me when times are tough? Should your face be bathed in brine today, it will be for the last time. Do not go.”
      """
      ),
      paragraph(
        """
        “I have to.”
      """
      ),
      paragraph(
        """
        The mother pushed her son away and the colour of her complexion matched that of her hair: “Go then, you traitor! High-and-mighty you may be, but before you taste the bitterness of death, you will return to me, your mother, regardless – same as everyone!”
      """
      ),
      paragraph(
        """
        The hero turned on his heel, dropped the pomegranate seed in the dust by the road and ruminated on things of the past. Then he started walking towards the agora where a lamentably celebratory gathering was taking place. In front of the elevated temple entrance stood Demetrius the priest observing as the crowd grew in size.
      """
      ),
      paragraph(
        """
        When all citizens were within earshot, he started exclaiming with his powerful voice: “People of Atalanta, my compatriots, friends in mind and spirit, my kin. You, who have times innumerable proved your perseverance and steadfastness before the scourge hanging above our city like a mythical sword, must again face the ebb of the favour of the Mother of Seasons. But do not fret, for with every victim, with every failure we draw closer to understanding why such fate befalls us good citizens. And it is perhaps today when one fortunate individual from your ranks brings us Great Hope!”
      """
      ),
      paragraph(
        """
        And the crowd clinging onto every single one of the priest’s words down on the town square rejoiced and started cheering: “Great Hope!”
      """
      ),
      paragraph(
        """
        The priest moved to a nearby cast iron cauldron with a golden rim containing slips with the city’s inhabitants’ names, and again looked at the enthusiasts below. “Is there someone among you to do this service and earn fatherland’s honour of their own accord?”
      """
      ),
      paragraph(
        """
        The crowd suddenly turned silent. The wind swished through empty windows and the sun itself seemed to pressure the spectators into volunteering by its heat. Everyone felt it, even the paving stones were glad no one would roll them into the sea.
      """
      ),
      paragraph(
        """
        “I will draw lots then,” announced the devout man and picked a slip from the cauldron. He examined it and his expression contorted with dread. “What is this? There is no name here! The papyrus is blank!”
      """
      ),
      paragraph(
        """
        “That one’s mine,” shouted the hero after a moment’s hesitation. Having observed the event the whole time, he was now making his way through the crowd. “I’ll dive into the sea and tame the horrible monster.”
      """
      ),
      paragraph(
        """
        “Gods bless you, unknown saviour!” replied the priest with relief and stowed the slip into his robes. “Who are you, pray tell?”
      """
      ),
      paragraph(
        """
        “I’m the son of smith Filippos who refined metal and sparkled up our men at arms with armour, until he himself fell victim to the monster in his own shining masterpiece. Perhaps I’ll make it up to him after he exchanged the family hearth for the heat of the forge, and the city will be able to rest in peace again.”
      """
      ),
      paragraph(
        """
        With these words the tension in the crowd vanished, as if by magic, once it was revealed that the audience would be spared the astringent look at a despairer’s futile writhing in the clench of soldiers escorting them to the shore. These now took hold of the emptied cauldron instead and began moving to the designated location together with others led by the priest.
      """
      ),
      paragraph(
        """
        When they arrived, the hero received one more blessing and then everyone bore silent witness to the customary tragicomic scene where the men would repeatedly try to help him balance the inverted cauldron on his submerged head so that it would capture as much air as possible. After asserting he could uphold the cauldron alone following numerous failed attempts, he stepped into greater depth. Then, the surface of the water burst forth, and the monster dragged him into the dark.
      """
      ),
      title(""),
      paragraph(
        """
        The sudden yank almost lifts my round coffin off my hands, then the initial shock recedes and my only hope is that I don’t get gutted by either the monster or the quickly amassing water level. The air-buoyant cauldron as if it cut me off from the rest of the world, not even the worn out inside is visible in the darkness. I only hear quiet splashing of ripples on the walls dying off under the weight of deafening throbs as the whole body reluctantly relishes every last heartbeat. 
      """
      ),
      paragraph(
        """
        Under the increasing pressure, the surface of the water inside rises dismally, then the flow around stops and with great difficulty do I force my breath to hastily give up the licentious affluence it’s been used to my whole life.
      """
      ),
      paragraph(
        """
        I then hear a female voice that sounds like it’s coming from all directions outside, inside and nowhere at once.
      """
      ),
      paragraph(
        """
        “Are you here… to play? Others have come here, similarly blockheaded. They played by their own rules. They bored me. Now, they’re gone. And instead… I have you. I’m curious whether you swim with the current or fight against it as the others did.”
      """
      ),
      paragraph(
        """
        My heart stops. The monster is holding me fairly loosely and her grip doesn’t hurt but tightens when I try to move. Why’d such thing want to play? I’m bobbing in the cold and dark with arms sideways to hold the ring of the cauldron opening evenly around the waist. Up is down and down is up. Inverting the fragile balance would spell my end, but the endless ray down my core in dialogue with the perpendicular one around remains golden.
      """
      ),
      paragraph(
        """
        “Let’s not waste time then given the higher uppers are waiting so impatiently. My second question: Under what condition do you get from one question to the next?” the monster continues.
      """
      ),
      paragraph(
        """
        Is this already the riddle that sees the game over for me? And if it’s by now the second question, what was the first one? I try: “I proceed to the next question, when I answer the previous one.” Answer: “Not quite.”
      """
      ),
      paragraph(
        """
        Suddenly, in the murk of hopelessness, I spot a spark and add: “In that case it is necessary to answer the previous question truthfully.”
      """
      ),
      paragraph(
        """
        “He’s starting to toddle alright!” the monster responds and her inescapable voice continues: “Thirdly: Is it true that a person can only speak to me if they come from your city and let me seize them?”
      """
      ),
      paragraph(
        """
        “I assume the answer is yes.”
      """
      ),
      paragraph(
        """
        “Yes, you do assume that, but that was not my question. Is it true that only those who come from the city and let themselves be caught can talk to me?”
      """
      ),
      paragraph(
        """
        “Yes, that is true.”
      """
      ),
      paragraph(
        """
        “What a difference it makes when you use the magic word! Continuing with the fourth question: Under what condition do I release a person alive?”
      """
      ),
      paragraph(
        """
        Like a mouse rendered a plaything in a cat’s claws I say into the void of my life-giving trap: “When they answer truthfully all your questions, otherwise they die.”
      """
      ),
      paragraph(
        """
        “Exactly so. Fifth question: Is it true that I return this cauldron to the surface every time, either by itself when the person dies or together if they answer truthfully all of my questions and survive, but otherwise they can’t escape my grasp alone nor with another’s help?”
      """
      ),
      paragraph(
        """
        “Yes, that is true,” say I and wish for all the remaining challenges to be this trivial, although a voice deep inside clearly objecting that it can’t be. Despite the surrounding cold water, the air inside the cauldron is starting to feel unbearably hot.
      """
      ),
      paragraph(
        """
        “Impressive. After the first steps you are beyond halfway there, but from now on, the questions will be more profound. The sixth one is this: Is it possible for someone to answer all of my questions truthfully?”
      """
      ),
      paragraph(
        """
        Is this supposed to be a more profound question? A little flame of hope lights up in spite of the limited breathability and I answer determinedly: “Yes, that is true.”
      """
      ),
      paragraph(
        """
        “Seventh: Is the sentence ‘This sentence is false.’ true?”
      """
      ),
      paragraph(
        $$"""
        %1$s What? But that doesn't make… this can’t be… true…! If the sentence is false, then it can’t admit its mendacity, and if it’s true, it can’t contradict it, but in both cases it doesn’t make any sense.
      """, Style.EMPTY.withItalic(true), "My head surges with thoughts." to Style.EMPTY
      ),
      paragraph(
        """
        I start jerking and jolting and strive to free myself from the unbreakable clasp until complete exhaustion, but in vain. I myself confirmed that I may not leave here alive without answering all questions. Shuddering, I cock my head backwards and look up in supplication where I am greeted by a glaring nothingness. I am lost like tears in rain, and my own salty rain is now dispersing into oblivion in the briny cradle of all life where I am to follow in a few moments.
      """
      ),
      paragraph(
        """
        Silence. From emptiness, the world collapses into inexistence, and I am carried away on waves of infinity. My consciousness is abraded again and again by the same thought. Have I ever lived outside the monster’s grasp? The events preceding this moment suddenly seem like a misty dream. That’s it, the world on the surface was my dream, I have now woken up and nothing awaits me, only death. In the dream I felt joy, sadness, pleasure and loss, but none of it was real. During my descent I reached an awakening and the reality revealed itself to me as it would to no one else, because no one else ever truly lived. I am myself.
      """
      ),
      paragraph(
        """
        And with this notion memories are flooding back, or perhaps old-new dreams are surfacing, as if my impending consonance with all sea currents kicked my mind into full gear so that it may for the last time savour the incredibly organised interplay of others possessed of the potential to cleave the living by both sword and word, to admire, intimidate, ridicule and regret, or carry out the action most indispensable, to grow numb, sleep and in the mind’s eye conjure things so intimately precious, or so also some of which one previously had no inkling.
      """
      ),
      title(""),
      paragraph("A grove of olives sprouts all ‘round,"),
      paragraph("the leaves may offer short shades’ share,"),
      paragraph("sweet incense wafting through the air,"),
      paragraph("the sun brands crackles in the ground."),
      paragraph(""),
      paragraph("A man so parched, his tongue – ablaze,"),
      paragraph("lies next to me. A rock-hard bed,"),
      paragraph("our comfort fills our hearts with dread,"),
      paragraph("ill omens speaks its golden glaze."),
      paragraph(""),
      paragraph("“Such gift you’re given is no jest,"),
      paragraph("creative power at its best,”"),
      paragraph("I say. “Why heed not our behest?"),
      paragraph("You offer scraps, why not the rest?”"),
      paragraph(""),
      paragraph("He says: “To pledge my neck’s disgrace,"),
      paragraph("watch people good or evil strive"),
      paragraph("to take apart what makes them thrive"),
      paragraph("‘til all but sorrow lose their face?"),
      paragraph(""),
      paragraph("They take their smidge but crave a lot."),
      paragraph("A gift? I cling to life distraught."),
      paragraph("My subtle touch, their single thought."),
      paragraph("I wish to leave my spell to rot.”"),
      paragraph(""),
      paragraph("“Perhaps another man most chaste"),
      paragraph("will grant your work a different taste.”"),
      paragraph("“That others steal and tenfold waste?”"),
      paragraph("“On risks a dreamer’s life is based.”"),
      paragraph(""),
      paragraph("Like time in flight, the image shifts."),
      paragraph("The waves are splashing starboard side,"),
      paragraph("the crew take deafness as their guide."),
      paragraph("I hear beasts singing on the cliffs."),
      paragraph(""),
      paragraph("“Oh sirens, beauty shed in tears,"),
      paragraph("while some adore, rest plug their ears."),
      paragraph("Sing false – no good, sing true – stoke fears,"),
      paragraph("yet new songs are what stray hearts steers.”"),
      paragraph(""),
      paragraph("To elsewhere move I in a breath."),
      paragraph("A grave dug freshly, bloated corpse."),
      paragraph("The sky burns hot, the earth absorbs."),
      paragraph("Who truth divine sought, fell to death."),
      paragraph(""),
      paragraph("“You valiant boy, who charred your sky? "),
      paragraph("A fraud whose lies they wouldn’t buy? "),
      paragraph("You shone in meekness, bled to fly."),
      paragraph("Will we forget you? ... When? ... And why?”"),
      paragraph(""),
      paragraph("A gloomy mist obscures the maimed."),
      paragraph("Dim candles gutter. I have found"),
      paragraph("a dungeon deeply underground"),
      paragraph("with souls of thinkers no more named."),
      paragraph(""),
      paragraph("“Why halted were your efforts skilled?"),
      paragraph("A thousand lifetimes unfulfilled,"),
      paragraph("you learned that time on youth is spilled,"),
      paragraph("yet fed the cycle pupils thrilled.”"),
      paragraph(""),
      paragraph("I saw all this with my own eyes;"),
      paragraph($$"%1$s – or just a dream", Style.EMPTY.withItalic(true), "But then a sight" to Style.EMPTY),
      paragraph("Comes hurling down like trains of thought", Style.EMPTY.withItalic(true)),
      paragraph("On rails of steel from ages lost", Style.EMPTY.withItalic(true)),
      paragraph("– some other time.", Style.EMPTY.withItalic(true)),
      paragraph("", Style.EMPTY.withItalic(true)),
      paragraph("A man and spouse greet newborn boy", Style.EMPTY.withItalic(true)),
      paragraph("But war then makes them leave to live", Style.EMPTY.withItalic(true)),
      paragraph("A death of theirs so he won’t his.", Style.EMPTY.withItalic(true)),
      paragraph("The forest shade reveals his cries", Style.EMPTY.withItalic(true)),
      paragraph("– in the nick of time.", Style.EMPTY.withItalic(true)),
      paragraph("", Style.EMPTY.withItalic(true)),
      paragraph("A man gone hunting finds him there", Style.EMPTY.withItalic(true)),
      paragraph("Suspicious of his birth, his name,", Style.EMPTY.withItalic(true)),
      paragraph("He takes him home to sad wife’s grief.", Style.EMPTY.withItalic(true)),
      paragraph("They see him grow up fond of sea", Style.EMPTY.withItalic(true)),
      paragraph("", Style.EMPTY.withItalic(true)),
      paragraph("A flash of human kindness sprung", Style.EMPTY.withItalic(true)),
      paragraph("Marks young man’s coming close abroad", Style.EMPTY.withItalic(true)),
      paragraph("Where parents real once met their end.", Style.EMPTY.withItalic(true)),
      paragraph("He unaware then sees marines", Style.EMPTY.withItalic(true)),
      paragraph("– for the first time.", Style.EMPTY.withItalic(true)),
      paragraph("", Style.EMPTY.withItalic(true)),
      paragraph("A northern land now beckons him", Style.EMPTY.withItalic(true)),
      paragraph("Where sea beloved freezes fast.", Style.EMPTY.withItalic(true)),
      paragraph("A village? Town. It sprawls below", Style.EMPTY.withItalic(true)),
      paragraph("A mountain manor, ancient tree ", Style.EMPTY.withItalic(true)),
      paragraph("– biding their time.", Style.EMPTY.withItalic(true)),
      paragraph("", Style.EMPTY.withItalic(true)),
      paragraph("Amidst the cold, the cold of night", Style.EMPTY.withItalic(true)),
      paragraph("Amidst the walls with windows none", Style.EMPTY.withItalic(true)),
      paragraph("I watch him work; I watch him fail.", Style.EMPTY.withItalic(true)),
      paragraph("He wakes, despairs, dreams quiet dreams", Style.EMPTY.withItalic(true)),
      paragraph("– time after time.", Style.EMPTY.withItalic(true)),
      title(""),
      paragraph(
        """
        And so, similarly dreamy I hover surrendered, floating in the decanted tears of human vanity, liberated from intent, liberated from duty, liberated from everything, liberated from freedom.
      """
      ),
      paragraph(
        """
        But not from life.
      """
      ),
      paragraph(
        """
        After a while I notice that although my breath has stopped some time ago, I’m still very much present in both body and mind and the monster is still firmly holding onto me.
      """
      ),
      paragraph(
        """
        “I… I’m still here?” I ask.
      """
      ),
      paragraph(
        """
        “I’m the one who asks questions here and you answer. And your adorers on the surface are on edge,” I hear.
      """
      ),
      paragraph(
        """
        “If I’m still alive and it wasn’t only a dream,” I think aloud and try to adjust to conscious breathing, “it means that for some reason I can’t die here. In the previous questions I proved that I’m getting out of here, and if I can’t fail, I must succeed.” The shocking realisation then hits me like a bolt from the blue: “Is… Am I… seriously… invincible?” 
      """
      ),
      paragraph(
        """
        “Oh, I wouldn’t go to such lengths as to call it invincibility. While you could, say, ‘breathe’ it, trust me, you really don’t want to know what it’s like to continuously drown in sea water. Come to think of it, you’ve heard and felt it down here once, you’ve done it a thousand times. But you are one special undying exception,” the monster flatters slyly. “So, what about that ‘This sentence is false.’”
      """
      ),
      paragraph(
        """
        “Well, since we’ve established it’s possible to truthfully answer all your questions, then… the sentence is true,” I say, about as sure as desert rainfall.
      """
      ),
      paragraph(
        """
        “See, when there’s a will, there’s a way. Eighth question: How could the citizens know I’d ask nine questions?”
      """
      ),
      paragraph(
        """
        The innocent query feels colder than ice. How…? “Because… because someone else who… is also immortal… visited you, answered your nine questions and left, even if… I still don’t know what the first question was.” 
      """
      ),
      paragraph(
        $$"""
        “What does it matter? I mean… %1$s last question: You’ve established some other immortal citizen’s been here and given true answers. So why have I not left the city and swum away?”
      """, Style.EMPTY, "Ehm," to Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        I come to understand the despicable duplicity of her who swore nothing: “You monster, you don’t follow any rules, you just want to sate your wrath and spread sorrow!”
      """
      ),
      paragraph(
        """
        The monster swiftly strengthens her grip, starts to shake me about and tells me off: “You liar, how dare you talk to me like this? Don’t you see that not even Atropos herself can cut your life’s thread? Your immortality is clear evidence of both your and my truthfulness, but it doesn’t prevent me from washing that dirty mouth of yours! But then you could hardly keep answering and we’d likely be stuck here forever, well, not that I’m objecting. Again, why have I not left the city?”
      """
      ),
      paragraph(
        """
        “Because… because… but it doesn’t make any sense…” Another shake unbridles my imagination. “So, the other immortal secretly passed your test and returned to the surface, but turned out to be a dangerous sadist who spread a false belief that you would only leave the city once someone accomplishes what he did. You’ve been searching for them this whole time as being immortal yourself means you’re the only one to be able to resolve this, but to no avail. People on the surface can’t hear your voice, so you couldn’t have informed them of your intention. The traitor revels in the bloodshed and agony they’ve been causing in the meantime and won’t stop until everyone is dead,” I reel off my explanation.
      """
      ),
      paragraph(
        """
        “You’ve arrived at the true interpretation. Go, return to the surface, recount truthfully everything you’ve come to learn here, find the immortal and bring them back to me. Then you have my word that none will see me here ever again.”
      """
      ),
      paragraph(
        """
        “But what does my target look like?” I ask.
      """
      ),
      paragraph(
        """
        “What do I know? I couldn’t spot their appearance in such dark and with a cauldron on top.”
      """
      ),
      paragraph(
        """
        “And how should I tell the others who you really are? What’s your name?”
      """
      ),
      paragraph(
        """
        “Call me Sea Empress.”
      """
      ),
      title(""),
      paragraph(
        """
        How much the people rejoiced when the hero returned! They lifted him onto their shoulders and shouted: “Long live the one who has kindled the Great Hope!”
      """
      ),
      paragraph(
        """
        The procession came back to the temple where the priest silenced the cries with a wave of his arms. “Tell us, saviour, how come you’ve endured underwater so long? Did Lady Courage herself take pity on you, she who shines from the brow of the king of the gods? What trickery did you face down under and is the monster truly gone?” he asked.
      """
      ),
      paragraph(
        """
        The hero told of everything that had transpired underwater and the crowd eyed him with a growing disbelief. He added: “You’ve heard what I had to say and I see your doubts. My success brings with it a heavy accusation and therefore I suggest the following: behead me so that the executioner’s axe decides impartially whether I deserve death or speak the truth.”
      """
      ),
      paragraph(
        """
        The priest traded surprise for insight and noted: “If everything you have thus said is a perverse lie, you are a dangerous traitor, a fool, or both, and you are deserving of death. If you live, however, we shall see your honest sacrifice and counterbalance your words upon scales with heavier weights.”
      """
      ),
      paragraph(
        """
        And the gathering roared in unison: “Off with his head!”
      """
      ),
      paragraph(
        """
        The hero was propped over the cauldron that was not to accommodate his final rest, and the slayer severed his neck in one fell stroke. Accompanied by a stormy clamour of the axe-struck metal, trails of blood gushed forth and pooled at the bottom of the cauldron where the head lay, whereas the body fell to the side. Shortly after, however, the head sprung out of the cauldron, stuck to the neck anew and the hero stood up straight again to the spectators’ unprecedented amazement.
      """
      ),
      paragraph(
        """
        “Behold, we could not have dreamed of a nobler redeemer,” said the priest and religiously wiped blood off the hero’s face. “But with great relief comes a great sadness. We see, friend, that you spoke truly of the traitor who feeds his false blessing with our lives. I ask you, all of you present here, how do we find among us the one who so unbecomingly lied about the nature of the tormentor that has been terrorising us? How do we recognise the conspirator who has committed atrocities uncountable on you, ones which saw the Olympian monarchs’ very own son faint?”
      """
      ),
      paragraph(
        """
        “How, how do we find them?” asked the people. 
      """
      ),
      paragraph(
        """
        “Only those who do not bleed cannot die,” exclaimed the priest, drew a dagger from his robes and stabbed the executioner in cold blood. He collapsed dead to the floor and beneath him spilled out a dark puddle.
      """
      ),
      paragraph(
        """
        A universal uproar burst out in an instant. Men and women stabbed, slashed and crushed and were being stabbed, slashed and crushed with hopes that someone might rise back up after a lethal blow. Screaming and whimpering pierced the heavens to the extent that even the gods upon the holy mountain were shaken down to the marrow of their immortal bones. The arena was flooded in scarlet and when only a few survivors remained, they scattered so as not to meet anyone else and along the way slaughter all men, women, children and animals they encountered. The city armoury was littered with more bodies than weapons and fewer limbs than bodies, as many massacre participants thought it of utmost importance to verify the validity of their results. Nevertheless, bloodthirstiness eventually led all beating hearts back to the town square where the final showdown ensued. The second-to-last unfortunate individual thrust a sickle into the last one’s abdomen and she slit his throat in return. They fell to the ground and with a varying zeal sailed to the other side of the dark river.
      """
      ),
      paragraph(
        """
        In front of the temple entrance the priest was kneeling in prayer and the hero stood frozen solid.
      """
      ),
      paragraph(
        """
        He must have known, the whole time he must have known and let the others do his own dirty work. For that he deserves the slowest and most pathetic death…
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        The frost in his body increases almost to the point of audible cracking. How is he supposed to punish someone who’s immortal? Giant eagles had gone extinct and a stone-adorned ring did not seem to do the gravity of the situation justice.
      """
      ),
      paragraph(
        """
        “Never, even in my wildest dreams, did I believe my lady would grant me such honour. Today’s harvest shall fill the cornucopia with which I shall replace my wicked heart in Elysium,” announces the priest who had snapped out of his sanctimonious trance.
      """
      ),
      paragraph(
        """
        “How could you… why?” stutters the hero.
      """
      ),
      paragraph(
        """
        “I know not what this Sea Empress is, I do not understand why it appeared here and why it behaved so. On the day of my consecration, I made a secret promise to bring my lady a harvest worthy of her admiration. Had I started reaping haphazardly in the streets, however, I would have been revealed, and my efforts would have been in vain, I was certain of that. My offerings have since been rather sparse, but today, you have done me and the Nurturing Mother a grand service. May this notion be of consolation to you till the end of all days, or at least until you find the one you seek,” said the priest and pierced his heart with the dagger. As he was departing, from the folds of his robe slipped a single lot with the name “Demetrius.”
      """
      ),
      paragraph(
        """
        There remained no words to be spoken. There was no one to speak them to, and simultaneously they had lost all meaning. The hero thought no more. There was nothing to anchor his thoughts in. He just stood there looking at the ground into which he was never to return, one which would never admit him. He simply stood there so, maybe for a day, maybe a century, maybe he never stood there in the first place. In any case, what the first arrivals in the city discovered were human remains after an event beyond explanation, and a single trail marked in dried-out bloody footprints leading from a decorative cauldron, containing blood that was still fresh, down the temple steps towards the shore and faded where the tide allowed the sea to wash the sand.
      """
      ),
      title(""),
      paragraph(
        """
        “Oh, wise teacher, a story of love we beseeched, and thou hast told that of hatred!”
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        He respondeth that what one considereth hatred, another may call love, and oppositely, and asketh who is to decide whose side truth taketh.
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        Among the listeners a skirmish, not quite dissimilar to the fabled, commenceth immediately: 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        “– How was the pastor ever to be convinced his scheme would reach fruition? 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – Why appended smiths not an extensive pipe into the cauldron’s end as aeration or eavesdropping mediator? 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – For bottom-up it would have inundated, you fool! 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – How profuse was the monster’s capital-capital originally? By one head scarcely would it fare, by two heads barely cling to life! 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – Through whom was a blacksmith’s son cultivated that he might read and write? 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – What preserved footpaths of gore persisting in the metropolis? Had it not rained? 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        – Compose yourselves! The tale offereth controversy in excess, yet judge verily; how could word of the tragedy from the deceased or the dispersed to the outside world be borne; a story therefore from its very foundations counterfeit it must be, it carrieth grains of truth none; thus, the state of our logic most noble lingereth unthreatened! Finally, hath any man alive set eyes upon an immortal one?” 
      """, Style.EMPTY.withItalic(true)
      ),
      paragraph(
        """
        The master sootheth: “Yes, yes, positively hath each of you truth of your own. Do however recollect that one who in sight of deceitful gain words like wires draweth out and twisteth will gather sweet fruit into his basket of ill-alloyed wicker. Whereas in the light of day, boasteth the charlatan his splendour, he fades in time by his own undoing. Then, in the murky dark undersea, atop the head don his work and undergo fair judgement may he not. And whilst the progeny of the caster of brittle earths, cauldron-shaped or otherwise, victims radiating industry’s aureole may be, hoping to quench their brilliance, the tainted oil of wicked lies merely reinforceth them both to capture that which aeons withstandeth :D”
      """, Style.EMPTY.withItalic(true)
      )
    )
  )

  fun featuresFor(entryKey: ResourceKey<ResearchEntry>): List<PageFeature> =
    contentByEntry[entryKey].orEmpty().let { features ->
      features.mapIndexed { index, feature ->
        feature.create(PageFeature.translationId(ResearchEntry.translationId(entryKey), index, features.lastIndex))
      }
    }

  fun translations(): Map<String, String> = buildMap {
    for ((entryKey, features) in contentByEntry) {
      val baseId = ResearchEntry.translationId(entryKey)
      features.forEachIndexed { index, feature ->
        feature.text?.let { put(PageFeature.translationId(baseId, index, features.lastIndex), it) }
        if (feature.placeholders.isNotEmpty()) {
          feature.placeholders.forEachIndexed { number, string ->
            put(PageFeature.translationId(baseId, index, features.lastIndex) + "-%${number + 1}\$s", string)
          }
        }
      }
    }
  }
}

private class FeatureDefinition(
  val text: String?,
  val placeholders: List<String> = emptyList(),
  val factory: (String) -> PageFeature,
) {
  fun create(translationId: String) = factory(translationId)
}

private fun title(text: String) = FeatureDefinition(text.normalize()) { translationId ->
  TitleFeature(Component.translatable(translationId).withStyle(ChatFormatting.BOLD))
}

private fun paragraph(
  text: String,
  style: Style = Style.EMPTY,
  vararg placeholders: Pair<String, Style> = emptyArray()
) = FeatureDefinition(text.normalize(), placeholders.map { it.first }) { translationId ->
  ParagraphFeature(separateComponentStyles(translationId, style, *placeholders.mapIndexed { index, placeholder ->
    Component.translatable("$translationId-%${index + 1}\$s").setStyle(placeholder.second)
  }.toTypedArray()))
}

private fun pageBreak() = FeatureDefinition(null) { PageBreakFeature() }

private fun figure(image: Texture, width: Int, height: Int, caption: String? = null) =
  FeatureDefinition(caption?.normalize()) { translationId ->
    FigureFeature(image, width, height, caption?.let { Component.translatable(translationId) })
  }

private fun String.normalize() = trimIndent().replace("\n", " ")

private val NO_FORMAT = Style.EMPTY
  .withBold(false)
  .withItalic(false)
  .withUnderlined(false)
  .withStrikethrough(false)
  .withObfuscated(false)
  .withColor(0x000000)

/** Creates a copy of the component that ignores any text formatting inherited from its parent. */
private fun Component.unformatted(): MutableComponent =
  copy().setStyle(style.applyTo(NO_FORMAT))

fun separateComponentStyles(key: String, baseStyle: Style, vararg placeholders: Component) =
  Component.translatable(key, *placeholders.map { it.unformatted() }.toTypedArray()).setStyle(baseStyle)
