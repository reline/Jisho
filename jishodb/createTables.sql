-- Entry
CREATE TABLE IF NOT EXISTS Entry(id INTEGER NOT NULL PRIMARY KEY)

-- Kanji
CREATE TABLE IF NOT EXISTS Kanji(value TEXT NOT NULL PRIMARY KEY)
-- Entry & Kanji bridge table
CREATE TABLE IF NOT EXISTS EntryKanji(entryId INTEGER NOT NULL, kanji TEXT NOT NULL, FOREIGN KEY(entryId) REFERENCES Entry(id), FOREIGN KEY(kanji) REFERENCES Kanji(value))
-- Reading
CREATE TABLE IF NOT EXISTS Reading(value TEXT NOT NULL PRIMARY KEY, isNotTrueReading INTEGER)
-- Kanji & Reading bridge table
CREATE TABLE IF NOT EXISTS KanjiReading(kanji TEXT NOT NULL, reading TEXT NOT NULL, FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(reading) REFERENCES Reading(value))
-- DictSense
CREATE TABLE IF NOT EXISTS DictSense(id INTEGER NOT NULL PRIMARY KEY)
-- Kanji & DictSense bridge table
CREATE TABLE IF NOT EXISTS KanjiDictSense(kanji TEXT NOT NULL, senseId INTEGER NOT NULL, FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(senseId) REFERENCES DictSense(id))
-- Translation
CREATE TABLE IF NOT EXISTS Translation(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
-- Kanji & Translation bridge table
CREATE TABLE IF NOT EXISTS KanjiTranslation(kanji TEXT NOT NULL, translation INTEGER NOT NULL, FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(translation) REFERENCES Translation(id))

-- KanjiInfo
CREATE TABLE IF NOT EXISTS KanjiInfo(value TEXT NOT NULL PRIMARY KEY)
-- Kanji & KanjiInfo bridge table
CREATE TABLE IF NOT EXISTS KanjiInfoKanji(kanji TEXT NOT NULL, info TEXT NOT NULL, FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(info) REFERENCES KanjiInfo(value))
-- KanjiPriority
CREATE TABLE IF NOT EXISTS KanjiPriority(value TEXT NOT NULL PRIMARY KEY)
-- Kanji & KanjiPriority bridge table
CREATE TABLE IF NOT EXISTS KanjiPriorityKanji(kanji TEXT NOT NULL, priority TEXT NOT NULL, FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(priority) REFERENCES KanjiPriority(value))

-- ReadingRestriction -- todo: should this reference Kanji?
CREATE TABLE IF NOT EXISTS ReadingRestriction(kanji TEXT NOT NULL PRIMARY KEY)
-- Reading & ReadingRestriction bridge table
CREATE TABLE IF NOT EXISTS ReadingRestrictionReading(reading TEXT NOT NULL, restriction TEXT NOT NULL, FOREIGN KEY(reading) REFERENCES Reading(value), FOREIGN KEY(restriction) REFERENCES ReadingRestriction(kanji))
-- ReadingInfo
CREATE TABLE IF NOT EXISTS ReadingInfo(value TEXT NOT NULL PRIMARY KEY)
-- Reading & ReadingInfo bridge table
CREATE TABLE IF NOT EXISTS ReadingInfoReading(reading TEXT NOT NULL, info TEXT NOT NULL, FOREIGN KEY(reading) REFERENCES Reading(value), FOREIGN KEY(info) REFERENCES ReadingInfo(value))
-- ReadingPriority
CREATE TABLE IF NOT EXISTS ReadingPriority(value TEXT NOT NULL PRIMARY KEY)
-- Reading & ReadingPriority bridge table
CREATE TABLE IF NOT EXISTS ReadingPriorityReading(reading TEXT NOT NULL, priority TEXT NOT NULL, FOREIGN KEY(reading) REFERENCES Reading(value), FOREIGN KEY(priority) REFERENCES ReadingPriority(value))

-- KanjiTag
CREATE TABLE IF NOT EXISTS KanjiTag(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & KanjiTag bridge table
CREATE TABLE IF NOT EXISTS DictSenseKanjiTag(senseId INTEGER NOT NULL, tag TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(tag) REFERENCES KanjiTag(value))
-- ReadingTag
CREATE TABLE IF NOT EXISTS ReadingTag(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & ReadingTag bridge table
CREATE TABLE IF NOT EXISTS DictSenseReadingTag(senseId INTEGER NOT NULL, tag TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(tag) REFERENCES ReadingTag(value))
-- PartOfSpeech
CREATE TABLE IF NOT EXISTS PartOfSpeech(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & PartOfSpeech
CREATE TABLE IF NOT EXISTS DictSensePartOfSpeech(senseId INTEGER NOT NULL, part TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(part) REFERENCES PartOfSpeech(value))
-- XReference
CREATE TABLE IF NOT EXISTS XReference(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & XReference
CREATE TABLE IF NOT EXISTS DictSenseXReference(senseId INTEGER NOT NULL, xref TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(xref) REFERENCES XReference(value))
-- Antonym
CREATE TABLE IF NOT EXISTS Antonym(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & Antonym
CREATE TABLE IF NOT EXISTS DictSenseAntonym(senseId INTEGER NOT NULL, antonym TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(antonym) REFERENCES Antonym(value))
-- Field
CREATE TABLE IF NOT EXISTS Field(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & Field bridge table
CREATE TABLE IF NOT EXISTS DictSenseField(senseId INTEGER NOT NULL, field TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(field) REFERENCES Field(value))
-- Misc
CREATE TABLE IF NOT EXISTS Misc(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & Misc bridge table
CREATE TABLE IF NOT EXISTS DictSenseMisc(senseId INTEGER NOT NULL, misc TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(misc) REFERENCES Misc(value))
-- SenseInfo
CREATE TABLE IF NOT EXISTS SenseInfo(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & SenseInfo
CREATE TABLE IF NOT EXISTS SenseInfoDictSense(senseId INTEGER NOT NULL, info TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(info) REFERENCES SenseInfo(value))
-- Source
CREATE TABLE IF NOT EXISTS Source(value TEXT NOT NULL PRIMARY KEY, language TEXT NOT NULL, type TEXT NOT NULL, isWaseieigo TEXT NOT NULL)
-- DictSense & Source bridge table
CREATE TABLE IF NOT EXISTS DictSenseSource(senseId INTEGER NOT NULL, source TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(source) REFERENCES Source(value))
-- Dialect
CREATE TABLE IF NOT EXISTS Dialect(value TEXT NOT NULL PRIMARY KEY)
-- DictSense & Dialect bridge table
CREATE TABLE IF NOT EXISTS DictSenseDialect(senseId INTEGER NOT NULL, dialect TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(dialect) REFERENCES Dialect(value))
-- Gloss
CREATE TABLE IF NOT EXISTS Gloss(value TEXT NOT NULL PRIMARY KEY, language TEXT NOT NULL, gender TEXT)
-- DictSense & Gloss bridge table
CREATE TABLE IF NOT EXISTS DictSenseGloss(senseId INTEGER NOT NULL, gloss TEXT NOT NULL, FOREIGN KEY(senseId) REFERENCES DictSense(id), FOREIGN KEY(gloss) REFERENCES Gloss(value))

-- NameType
CREATE TABLE IF NOT EXISTS NameType(value TEXT NOT NULL PRIMARY KEY)
-- Translation & NameType bridge table
CREATE TABLE IF NOT EXISTS TranslationNameType(translation INTEGER NOT NULL, type TEXT NOT NULL, FOREIGN KEY(translation) REFERENCES Translation(id), FOREIGN KEY(type) REFERENCES NameType(value))
-- TranslationDetail
CREATE TABLE IF NOT EXISTS TranslationDetail(value TEXT NOT NULL PRIMARY KEY)
-- Translation & TranslationDetail bridge table
CREATE TABLE IF NOT EXISTS TranslationDetailTranslation(translation INTEGER NOT NULL, detail TEXT NOT NULL, FOREIGN KEY(translation) REFERENCES Translation(id), FOREIGN KEY(detail) REFERENCES TranslationDetail(value))
-- Translation & XReference bridge table
CREATE TABLE IF NOT EXISTS TranslationXReference(translation INTEGER NOT NULL, xref TEXT NOT NULL, FOREIGN KEY(translation) REFERENCES Translation(id), FOREIGN KEY(xref) REFERENCES XReference(value))