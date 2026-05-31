import uuid
import random
from datetime import datetime, timedelta
from elasticsearch import Elasticsearch, helpers
from faker import Faker

# Inicijalizacija Faker-a na engleskom
fake = Faker('en_US')

# Konekcija ka lokalnom Elasticsearch-u (bez SSL-a i autentifikacije, prema tvom docker-compose)
es = Elasticsearch("http://localhost:9200")

NUM_RECORDS = 1000
NUM_PLAYERS = 100
NUM_SCOUTS = 15

# Generisanje zajedničkih ID-jeva kako bi podaci bili relacioni
player_ids = [str(uuid.uuid4()) for _ in range(NUM_PLAYERS)]
scout_ids = [str(uuid.uuid4()) for _ in range(NUM_SCOUTS)]

# Rečnici za generisanje smislenih fudbalskih fudbalskih tekstova
tags_pool = ["box-to-box", "playmaker", "target-man", "injury-prone", "leader", "wonderkid", "speedster", "tactical-genius", "inconsistent", "work-rate", "poacher", "sweeper"]
sources_pool = ["BBC Sport", "Sky Sports", "The Athletic", "Goal.com", "ESPN FC", "L'Equipe", "Marca"]
tactical_notes_pool = [
    "Excellent spatial awareness in the final third.", "Struggles with offside trap positioning.", 
    "Great at dropping deep to receive the ball.", "Maintains high pressing intensity for 90 minutes.",
    "Needs to improve tracking back on counter-attacks.", "Brilliant at exploiting half-spaces."
]
psycho_pool = [
    "Natural born leader, vocal on the pitch.", "Can lose temper under high pressure.",
    "Highly disciplined and focused.", "Shows signs of complacency against weaker teams.",
    "Great dressing room presence, highly motivated."
]
weaknesses_pool = [
    "Lacks aerial dominance.", "Poor weak foot ability.", "Prone to muscle injuries.",
    "Stamina drops significantly after 70 minutes.", "Aggressive tackling leads to unnecessary cards.",
    "Slow decision making under high press."
]

def generate_reports():
    actions = []
    for _ in range(NUM_RECORDS):
        doc = {
            "_index": "reports",
            "_id": str(uuid.uuid4()),
            "_source": {
                "reportId": str(uuid.uuid4()),
                "playerId": random.choice(player_ids),
                "scoutId": random.choice(scout_ids),
                "psychologicalProfile": f"{random.choice(psycho_pool)} {fake.sentence(nb_words=6)}",
                "tacticalNotes": f"{random.choice(tactical_notes_pool)} {fake.sentence(nb_words=8)}",
                "weaknesses": f"{random.choice(weaknesses_pool)} {fake.sentence(nb_words=5)}",
                "tags": random.sample(tags_pool, k=random.randint(1, 4)),
                "overallRating": random.randint(3, 10),
                # Generisanje datuma u poslednje 2 godine
                "createdAt": (datetime.utcnow() - timedelta(days=random.randint(0, 730))).isoformat(timespec='seconds') + "Z"
            }
        }
        actions.append(doc)
    return actions

def generate_articles():
    actions = []
    for _ in range(NUM_RECORDS):
        doc = {
            "_index": "articles",
            "_id": str(uuid.uuid4()),
            "_source": {
                "articleId": str(uuid.uuid4()),
                "playerId": random.choice(player_ids),
                "title": fake.catch_phrase().title() + " - " + random.choice(["Transfer Rumor", "Injury Update", "Match Performance", "Interview"]),
                "content": fake.text(max_nb_chars=500),
                "source": random.choice(sources_pool),
                # Sentiment score od -1.0 (veoma negativno) do 1.0 (veoma pozitivno)
                "sentimentScore": round(random.uniform(-1.0, 1.0), 2),
                "publishDate": (datetime.utcnow() - timedelta(days=random.randint(0, 730))).isoformat(timespec='seconds') + "Z"
            }
        }
        actions.append(doc)
    return actions

if __name__ == "__main__":
    print("⏳ Generisanje skautskih izveštaja (Reports)...")
    reports_data = generate_reports()
    success_reports, _ = helpers.bulk(es, reports_data)
    print(f"✅ Uspešno ubačeno {success_reports} izveštaja u 'reports' indeks.")

    print("⏳ Generisanje medijskih članaka (Articles)...")
    articles_data = generate_articles()
    success_articles, _ = helpers.bulk(es, articles_data)
    print(f"✅ Uspešno ubačeno {success_articles} članaka u 'articles' indeks.")
    
    print("🚀 Svi podaci su spremni!")