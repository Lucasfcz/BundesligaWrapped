// Lightweight club lookup — used ONLY to resolve readable names from DFL IDs
// and to build the crest URL. All wrapped content (trophies, motto, colors)
// comes from the backend response.

export type ClubLookup = {
  clubId: string;
  threeLetterCode: string;
  shortName: string;
  longName: string;
};

export const CLUBS: Record<string, ClubLookup> = {
  "DFL-CLU-00000G": { clubId: "DFL-CLU-00000G", threeLetterCode: "FCB", shortName: "Bayern", longName: "FC Bayern München" },
  "DFL-CLU-000007": { clubId: "DFL-CLU-000007", threeLetterCode: "BVB", shortName: "Dortmund", longName: "Borussia Dortmund" },
  "DFL-CLU-00000B": { clubId: "DFL-CLU-00000B", threeLetterCode: "B04", shortName: "Leverkusen", longName: "Bayer 04 Leverkusen" },
  "DFL-CLU-00000F": { clubId: "DFL-CLU-00000F", threeLetterCode: "SGE", shortName: "Frankfurt", longName: "Eintracht Frankfurt" },
  "DFL-CLU-00000D": { clubId: "DFL-CLU-00000D", threeLetterCode: "VFB", shortName: "Stuttgart", longName: "VfB Stuttgart" },
  "DFL-CLU-00000E": { clubId: "DFL-CLU-00000E", threeLetterCode: "SVW", shortName: "Bremen", longName: "SV Werder Bremen" },
  "DFL-CLU-000004": { clubId: "DFL-CLU-000004", threeLetterCode: "BMG", shortName: "M'gladbach", longName: "Borussia Mönchengladbach" },
  "DFL-CLU-00000S": { clubId: "DFL-CLU-00000S", threeLetterCode: "BOC", shortName: "Bochum", longName: "VfL Bochum 1848" },
  "DFL-CLU-00000A": { clubId: "DFL-CLU-00000A", threeLetterCode: "SCF", shortName: "Freiburg", longName: "Sport-Club Freiburg" },
  "DFL-CLU-000006": { clubId: "DFL-CLU-000006", threeLetterCode: "M05", shortName: "Mainz", longName: "1. FSV Mainz 05" },
  "DFL-CLU-00000H": { clubId: "DFL-CLU-00000H", threeLetterCode: "STP", shortName: "St. Pauli", longName: "FC St. Pauli" },
  "DFL-CLU-000003": { clubId: "DFL-CLU-000003", threeLetterCode: "WOB", shortName: "Wolfsburg", longName: "VfL Wolfsburg" },
  "DFL-CLU-000010": { clubId: "DFL-CLU-000010", threeLetterCode: "FCA", shortName: "Augsburg", longName: "FC Augsburg" },
  "DFL-CLU-00000V": { clubId: "DFL-CLU-00000V", threeLetterCode: "FCU", shortName: "Union Berlin", longName: "1. FC Union Berlin" },
  "DFL-CLU-000N5P": { clubId: "DFL-CLU-000N5P", threeLetterCode: "KSV", shortName: "Kiel", longName: "Holstein Kiel" },
  "DFL-CLU-000002": { clubId: "DFL-CLU-000002", threeLetterCode: "TSG", shortName: "Hoffenheim", longName: "TSG Hoffenheim" },
  "DFL-CLU-000018": { clubId: "DFL-CLU-000018", threeLetterCode: "FCH", shortName: "Heidenheim", longName: "1. FC Heidenheim 1846" },
  "DFL-CLU-000017": { clubId: "DFL-CLU-000017", threeLetterCode: "RBL", shortName: "Leipzig", longName: "RB Leipzig" },
};

const DFL_ID_RE = /^DFL-CLU-[A-Z0-9]+$/i;

/** If `value` looks like a DFL ID, swap it for the readable long name. Otherwise return as-is. */
export function resolveTeamName(value: string | undefined | null, fallbackId?: string): string {
  if (value && !DFL_ID_RE.test(value)) return value;
  const id = (value && DFL_ID_RE.test(value) ? value : fallbackId) ?? "";
  return CLUBS[id]?.longName ?? value ?? "";
}

/** Bundesliga.com hosts club crests at this CDN keyed by DFL club id. */
export function crestUrl(clubId: string): string {
  return `https://www.bundesliga.com/assets/clublogo/${clubId}.svg`;
}

/** Returns the crest URL or null when no club id is provided. */
export function getClubLogo(clubId: string | undefined | null): string | null {
  if (!clubId) return null;
  return crestUrl(clubId);
}
