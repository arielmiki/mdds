package com.scmanis.mdds.agent;

import java.util.*;
import java.util.stream.Collectors;

public class MDDSAgent {
    private List<Sentence> sentences;
    private Map<String, Literal> literalMap;
    private Set<Literal> inferred;
    private static MDDSAgent instance;
    private boolean isShuffle;
    private int questionCounter;


    private MDDSAgent(List<Literal> literals) {
        this.literalMap = literals.stream().collect(Collectors.toMap(Literal::getIdentifier, symbol -> symbol));
        sentences = new ArrayList<>();
        inferred = new HashSet<>();
        isShuffle = false;
        questionCounter = 0;
    }

    public static MDDSAgent getInstance() {
        if (instance == null) {
            List<Literal> literalList = new ArrayList<>();


            //CLUSTER A
            //Paranoid
            literalList.add(new Diagnosis("Paranoid", "Resah, curiga, dan tidak percayaan kepada orang lain dengan menganggap orang lain memiliki motif jahat tanpa sebab."));
            literalList.add(new Symptom("A01", "Ketika saya menemukan benda milik orang lain di jalan, saya merasa barang tersebut akan mencelakai saya."));
            literalList.add(new Symptom("A02", "Saya merasa teman-teman saya akan meninggalkan saya jika saya tertimpa masalah."));
            literalList.add(new Symptom("A03", "Saya cenderung enggan memberitahukan rahasia saya kepada orang lain karena takut disalahgunakan."));
            literalList.add(new Symptom("A04", "Saya tidak mau berkomunikasi dengan orang asing yang menegur saya di jalan."));
            literalList.add(new Symptom("A05", "Saya sulit melupakan perbuatan-perbuatan orang yang jahat kepada saya."));
            literalList.add(new Symptom("A06", "Jika ada orang yang mencemooh saya, maka saya akan langsung membalas mereka."));
            literalList.add(new Symptom("A07", "Saya curiga akan kesetiaan pasangan saya jika pasangan saya berkomunikasi dengan lawan jenisnya."));

            //Schizoid
            literalList.add(new Diagnosis("Schizoid", "Menghindar dari hubungan sosial dan membatasi ekspresi emosi interpersonal."));
            literalList.add(new Symptom("A08", "Saya tidak suka terlalu dekat dengan seseorang."));
            literalList.add(new Symptom("A09", "Saya lebih suka melakukan aktivitas yang tidak berhubungan dengan orang lain seperti bermain games."));
            literalList.add(new Symptom("A10", "Saya tidak tertarik untuk memiliki hubungan seksual dengan seseorang."));
            literalList.add(new Symptom("A11", "Saya hanya memiliki sedikit kepuasan dalam malakukan suatu aktivitas."));
            literalList.add(new Symptom("A12", "Saya tidak memiliki teman dekat selain keluarga saya."));
            literalList.add(new Symptom("A13", "Saya tidak memperdulikan kritik dan pujian kepada saya."));
            literalList.add(new Symptom("A14", "Saya jarang sekali menunjukan eksperesi saya kepada orang lain seperti tersenyum atau marah."));

            //Schizotypal
            literalList.add(new Diagnosis("Schizotypal", "Ketidaknyamanan akut terhadap hubungan dekat karena perbedaan kognitif dan perbedaan kelakuan."));
            literalList.add(new Symptom("A15", "Saya percaya suatu kejadian bisa saja merupakan dampak dari hal-hal gaib."));
            literalList.add(new Symptom("A16", "Saya mempercayai hal-hal berbau mistis dan fenomena paranormal."));
            literalList.add(new Symptom("A17", "Saya suka melakukan ritual khusus seperti membunyikan klakson sebelum masuk ke terowongan."));
            literalList.add(new Symptom("A18", "Saya suka memakai metafora, hiperbola, dan gaya bahasa lain dalam berbicara."));
            literalList.add(new Symptom("A19", "Saya curiga kerabat kerja saya menjelekan saya di depan atasan saya."));
            literalList.add(new Symptom("A20", "Saya membatasi diri saya dalam berbicara dan bernegosiasi dengan orang lain."));
            literalList.add(new Symptom("A21", "Jika saya datang ke suatu pesta, saya cenderung menghindari kontak mata dengan orang."));
            literalList.add(new Symptom("A22", "Saya hanya memiliki sedikit, bahkan tidak ada teman dekat selain keluarga saya."));
            literalList.add(new Symptom("A23", "Saya sangat gugup dalam berkomunikasi dengan orang lain terutama orang-orang yang tidak saya kenal."));


            //CLUSTER B
            //Antisocial
            literalList.add(new Diagnosis("Antisocial", "Tidak memperdulikan dan melanggar hak orang lain"));
            literalList.add(new Symptom("B11", "Saya sering dihukum keras karena melanggar aturan"));
            literalList.add(new Symptom("B12", "Saya merasa senang ketika saya berhasil membohongi orang dan mendapatkan yang saya inginkan"));
            literalList.add(new Symptom("B13", "Saya pernah memiliki banyak pasangan(kekasih) di masa lalu (sekaligus maupun tidak)."));
            literalList.add(new Symptom("B14", "Jika ada yang membuat saya marah, saya mudah sekali untuk marah dan bersikap agresif"));
            literalList.add(new Symptom("B15", "Saya sering mengebut atau bahkan membawa mobil saat saya mabuk"));
            literalList.add(new Symptom("B16", "Saya pernah menganggur dalam periode waktu yang cukup lama, atau saya beberapa kali berhutang"));
            literalList.add(new Symptom("B17", "Jika ada orang yang mengeluh karena suatu kemalangan atau kedukaan, hal yang pertama saya pikirkan adalah bahwa itu hal yang biasa dan orang tersebut harusnya bisa menerima itu"));

            //Borderline
            literalList.add(new Diagnosis("Borderline", "Ketidakstabilan dalam hubungan interpersonal, gambaran diri, dan impulsivitas"));
            literalList.add(new Symptom("B21", "Jika ada yang membatalkan janji pertemuan atau bahkan terlambat sebentar saja, saya akan marah"));
            literalList.add(new Symptom("B22", "Saya ingin memiliki pasangan yang ideal dan hanya akan peduli terhadapnya jika dia ada saat saya butuhkan"));
            literalList.add(new Symptom("B23", "Saya beberapa kali berganti rencana karir di masa depan saya"));
            literalList.add(new Symptom("B24", "Saya beberapa kali terlalu banyak makan, mengebut, atau bahkan melakukan hal-hal berbahaya lain (judi, penyalahgunaan obat-obatan) hanya karena impulsivitas"));
            literalList.add(new Symptom("B25", "Saya pernah melakukan \"cutting\" ataupun percobaan bunuh diri"));
            literalList.add(new Symptom("B26", "Saya sering mengalami anxiety yang hanya berlangsung dalam hitungan jam dan tidak sampai berhari-hari"));
            literalList.add(new Symptom("B27", "Saya seringkali mudah bosan sehingga sering mencari kegiatan lain"));
            literalList.add(new Symptom("B28", "Saya sulit mengontrol emosi saya\n"));

            //Histrionic
            literalList.add(new Diagnosis("Histrionic", "Emosionalitas dan cari perhatian yang berlebihan"));
            literalList.add(new Symptom("B31", "Jika orang-orang sepertinya tidak memperhatikan saya, saya cenderung melakukan sesuatu yang akan membuat mereka memperhatikan saya"));
            literalList.add(new Symptom("B32", "Saya sering memiliki penampilan yang terbilang orang sangat provokatif dan menggoda bahkan ketika tidak bersama pasangan saya"));
            literalList.add(new Symptom("B33", "Banyak orang yang mengatakan bahwa ekpresi emosional saya tergolong dangkal dan sering berganti-ganti"));
            literalList.add(new Symptom("B34", "Saya sangat ingin membuat orang lain terkesan dengan penampilan saya sehingga saya meluangkan waktu, tenaga, dan uang yang sangat banyak hanya untuk penampilan saya"));
            literalList.add(new Symptom("B35", "Ketika saya mengidolakan seseorang, saya cenderung memujinya dengan baik namun jika ditanya apa saja alasannya saya tidak bisa memberikan detil yang lebih"));
            literalList.add(new Symptom("B36", "Saya tidak malu untuk bersikap terlalu dramatis sampai-sampai banyak orang yang berprasangka bahwa semua itu hanyalah kebohongan"));
            literalList.add(new Symptom("B37", "Orang yang saya anggap teman dekat saya ternyata hanya menganggap saya teman biasa saja"));

            //Narcissistic
            literalList.add(new Diagnosis("Narcissistic", "Kebutuhan akan kekaguman dari orang lain dan kurangnya rasa empati"));
            literalList.add(new Symptom("B41", "Saya ingin sekali orang lain tahu tentang pencapaian dan talenta yang saya punya"));
            literalList.add(new Symptom("B42", "Saya sering membayangkan diri saya pada keadaan sukses dan penuh kekuasaan secara berlebihan"));
            literalList.add(new Symptom("B43", "Ketika orang lain tidak mengerti apa yang saya maksud, saya merasa itu hal wajar karena saya menganggap hanya orang-orang hebat saja yang mengerti"));
            literalList.add(new Symptom("B44", "Saya akan merasa sangat marah dan dipermalukan apabila ada orang yang mengkritik saya, dan saya ingin membalasnya suatu saat nanti"));
            literalList.add(new Symptom("B45", "Saya akan sangat marah ketika kebutuhan saya tidak terpenuhi"));
            literalList.add(new Symptom("B46", "Saya merasa bahwa pasangan yang saya punya harus bisa mengangkat derajat saya di mata orang-orang lain"));
            literalList.add(new Symptom("B47", "Saya bisa tanpa sadar menyombongkan hubungan yang saya miliki sekarang di depan mantan kekasih saya tanpa mempedulikan perasaanya"));
            literalList.add(new Symptom("B48", "Saya merasa iri terhadap orang yang lebih hebat dari saya dan merasa bahwa banyak orang yang juga iri terhada saya"));
            literalList.add(new Symptom("B49", "Banyak orang yang beranggapan bahwa saya adalah orang yang arogan"));

            //CLUSTER C
            //Avoidant
            literalList.add(new Diagnosis("Avoidant", "Pola yang meliputi penghambatan sosial, perasaan kekurangan, dan hipersensitif terhadap evaluasi negatif"));
            literalList.add(new Symptom("C01", "Saya lebih suka tugas/pekerjaan yang dilakukan sendiri"));
            literalList.add(new Symptom("C02", "Saya hanya ingin bertemu dengan orang-orang yang pasti menerima saya"));
            literalList.add(new Symptom("C03", "Saya lebih pendiam di relasi yang intim (intimate relationship) karena takut dipermalukan atau ditertawakan"));
            literalList.add(new Symptom("C04", "Saya cenderung menghindari situasi sosial karena takut akan mengatakan atau melakukan hal yang memalukan"));
            literalList.add(new Symptom("C05", "Saya sering merasa 'kurang' dibandingkan dengan orang lain. C06\", \"Saya tidak merasa diri saya menarik, tidak layak, dan rendah mutunya dari orang lain"));
            literalList.add(new Symptom("C06", "Jika saya telat 30 mnt, kelasnya hanya 50 mnt, dan ini pertama kalinya saya terlambat saya memilih tidak masuk karena takut dikritik atau dibicarakan."));

            //Dependent
            literalList.add(new Diagnosis("Dependent", "Kebutuhan yang berlebih untuk diurus yang mengarah ke perilaku yang patuh dan 'melekat' dan rasa takut dari perpisahan"));
            literalList.add(new Symptom("C11", "Membuat keputusan sehari-hari tanpa minta pendapat orang lain menyulitkan saya."));
            literalList.add(new Symptom("C12", "Untuk keputusan yang sangat penting tentang hidup saya, saya percayakan untuk orang terdekat saya."));
            literalList.add(new Symptom("C13", "Saya sulit mengungkapkan pernyataan tidak setuju dengan orang-orang yang sudah dekat karena takut kehilangan dukungan/persetujuan dari mereka."));
            literalList.add(new Symptom("C14", "Saya tidak suka mengambil inisiatif (memulai project atau tugas)"));
            literalList.add(new Symptom("C15", "Saya tidak keberatan untuk melakukan hal-hal yang tidak saya sukai untuk teman dekat saya karena sangat penting bagi saya untuk tetap dekat dengan mereka."));
            literalList.add(new Symptom("C16", "Saya tidak tahan sendirian"));
            literalList.add(new Symptom("C17", "Jika hubungan pertemanan saya berakhir, saya bisa saja secara mendadak mencari teman penggantinya."));
            literalList.add(new Symptom("C18", "Saya sering ketakutan akan dilepas, disuruh untuk berpikir sendiri"));
            literalList.add(new Symptom("C19", "Saya lebih pilih jadi wakil ketimbang jadi ketua"));

            //OCD
            literalList.add(new Diagnosis("OCD", "Pola yang meliputi pikiran yang dipenuhi oleh perfeksionisme, keterurutan, mental dan interpersonal control, dengan mengorbankan fleksibilitas, keterbukaan, dan efisiensi."));
            literalList.add(new Symptom("C21", "Saya salah menaruh tempat untuk mencatat hal-hal yang harus dilakukan. Yang saya lakukan adalah mencari catatannya"));
            literalList.add(new Symptom("C22", "Saya sering kali molor waktu dalam mengumpulkan tugas karena sering menulis ulang bagian-bagian yang kurang 'bagus'"));
            literalList.add(new Symptom("C23", "Saya mengalokasikan waktu untuk mengerjakan tugas, tetapi tidak mengalokasikan waktu untuk santai"));
            literalList.add(new Symptom("C24", "Pada suatu kasus, ada mobil menerobos lampu merah. Ternyata mobil tersebut buru-buru ke rumah sakit karena membawa orang yang pingsan. Menurut saya mobil itu tetap salah apapun alasannya\""));
            literalList.add(new Symptom("C25", "Seringkali saya menyimpan benda-benda yang usang dan tidak dipakai lagi karena mungkin saja saya akan butuh di masa depan.\""));
            literalList.add(new Symptom("C26", "Saya agak ragu untuk melimpahkan tugas ke orang lain, kecuali kalau mereka mengumpulkan dengan caranya"));
            literalList.add(new Symptom("C27", "Saya sangat hemat dan bisa dibilang pelit. Uang perlu disimpan karena untuk hal-hal tidak terduga terjadi (cth. bencana)"));
            literalList.add(new Symptom("C28", "Banyak yang bilang saya kaku dan keras kepala"));
            literalList.add(new Symptom("C29", "Saya suka kesal dengan hal-hal yang saya tidak bisa kendalikan"));

            instance = new MDDSAgent(literalList);
            instance.tell("A01^A02^A03^A04^A05^A06^A07=>Paranoid;4");
            instance.tell("A08^A09^A10^A11^A12^A13^A14=>Schizoid;4");
            instance.tell("A15^A16^A17^A18^A19^A20^A21^A22^A23=>Schizotypal;4");
            instance.tell("B11^B12^B13^B14^B15^B16^B17=>Antisocial;3");
            instance.tell("B21^B22^B23^B24^B25^B26^B27^B28=>Borderline;5");
            instance.tell("B31^B32^B33^B34^B35^B36^B37=>Histrionic;5");
            instance.tell("B41^B42^B43^B44^B45^B46^B47^B48^B49=>Narcissistic;5");
            instance.tell("C01^C02^C03^C04^C05^C06=>Avoidant;4");
            instance.tell("C11^C12^C13^C14^C15^C16^C17^C18^C19=>Dependent;5");
            instance.tell("C21^C22^C23^C24^C25^C26^C27^C28^C29=>OCD;4");
        }
        return instance;
    }

    public void choose(String diagnosis) {
        for (Sentence sentence: sentences.stream()
                .filter(sentence -> !sentence.getConsequence().equals(literalMap.get(diagnosis)))
                .collect(Collectors.toList())) {
            sentences.remove(sentence);
        }
    }

    public Literal getAskedLiteral() {
        if (!isShuffle ) {
            Collections.shuffle(sentences);
            isShuffle = true;
        }
        if (sentences.isEmpty()) {
            return null;
        }
        questionCounter++;
        return sentences.get(0).getAskedLiteral();
    }

    public Literal getCurrentLiteral() {
        return sentences.get(0).getAskedLiteral();
    }

    public MDDSAgentMemento save() {
        return new MDDSAgentMemento(sentences, inferred, isShuffle, questionCounter);
    }

    public void restore(MDDSAgentMemento memento) {
        this.sentences = memento.getSentences();
        this.inferred = memento.getInferred();
        this.isShuffle = memento.isShuffle();
        this.questionCounter = memento.getQuestionCounter();
    }


    public String answerLiteral(Literal literal, boolean answer) {
        if (answer) {
            return tell(literal.getIdentifier());
        }
        List<Sentence> linkedSentences = sentences.stream()
                .filter(sentence -> !(sentence instanceof AtomicSentence) && sentence.containSymbol(literal)).collect(Collectors.toList());
        for (Sentence sentence : linkedSentences) {
            if (sentence.wrongLiteral(literal)) {
                sentences.remove(sentence);
            }
        }
        return "Sehat";
    }


    public String tell(String raw) {
        Sentence sentence = parse(raw);
        if (sentence instanceof AtomicSentence) {
            return infer((AtomicSentence) sentence);
        }
        sentences.add(sentence);
        return "Sehat";
    }

    public String infer(AtomicSentence atomicSentence) {
        Queue<Literal> agenda = new LinkedList<>();
        agenda.offer(atomicSentence.getConsequence());
        while (!agenda.isEmpty()) {
            Literal literal = agenda.poll();
            if (!inferred.contains(literal)) {
                inferred.add(literal);
                List<Sentence> linkedSentences = sentences.stream()
                        .filter(sentence -> !(sentence instanceof AtomicSentence) && sentence.containSymbol(literal)).collect(Collectors.toList());
                for (Sentence sentence : linkedSentences) {
                    sentence.proof(literal);
                    if (sentence.inferred()) {
                        sentences.remove(sentence);
                        inferred.add(sentence.getConsequence());
                        agenda.add(sentence.getConsequence());
                        if (sentence.getConsequence() instanceof Diagnosis) {
                            return ((Diagnosis) sentence.getConsequence()).getDiagnosis();
                        }
                    }

                }
            }
        }
        return "Sehat";
    }


    public boolean ask(String raw) {
        Literal query = literalMap.get(raw);
        return inferred.contains(query);
    }

    private Sentence parse(String raw) {
        String[] clauses = raw.split(";");
        if (clauses.length == 2) {
            int threshold = Integer.parseInt(clauses[1]);
            clauses = clauses[0].split("=>");
            return new Sentence(parseAntecedent(clauses[0]), literalMap.get(clauses[1]), threshold);
        }
        clauses = clauses[0].split("=>");
        if (clauses.length < 2) {
            return new AtomicSentence(literalMap.get(clauses[0]));
        }
        return new Sentence(parseAntecedent(clauses[0]), literalMap.get(clauses[1]));

    }

    private List<Literal> parseAntecedent(String raw) {
        List<Literal> antecedent = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(raw, "^");
        while (st.hasMoreTokens()) {
            Literal literal = literalMap.get(st.nextToken());
            if (!inferred.contains(literal)) {
                antecedent.add(literal);
            }
        }
        return antecedent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Sentence> it = sentences.listIterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
