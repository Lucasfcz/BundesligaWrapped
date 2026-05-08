export type WrappedData = {
  userId: string;
  fanIdentity: {
    fanType: string;
    totalEngagements: number;
    articles: number;
    videos: number;
    stories: number;
    matchCenter: number;
  };
  mostActiveMonth: { month: string; matchCenterCount: number };
  favoriteClub: {
    clubName: string;
    shortName: string;
    primaryColor: string;
    secondaryColor: string;
    clubId?: string;
    threeLetterCode?: string;
    /** Optional trophy cabinet, sent by backend. Frontend renders whatever is provided. */
    trophies?: Array<{ count: number; label: string; icon?: string }>;
    /** Optional club motto. */
    motto?: { native: string; english?: string };
    /** Optional season stats — rendered on Slide 3 when present. */
    seasonStats?: {
      matchesPlayed: number;
      wins: number;
      draws: number;
      losses: number;
      goalsScored: number;
    };
  };
  topPlayer: {
    playerName: string;
    goals: number;
    assists: number;
    /** Optional club info for the player — backend may send id and/or name. */
    clubId?: string;
    clubName?: string;
    clubShortName?: string;
  };
  seasonHighlight: {
    homeTeam: string;
    guestTeam: string;
    result: string;
    spectators: number;
    matchDay: number;
    /** Optional explicit IDs so the frontend can resolve readable names if homeTeam/guestTeam come back as raw DFL IDs. */
    homeTeamId?: string;
    guestTeamId?: string;
  };
  narrative: { text: string };
};

export const MOCK: WrappedData = {
  userId: "79BC2FC64A88826C58780EC2AFE828B206467E872990919C211BEA33A4869B04",
  fanIdentity: {
    fanType: "Match Center Addict",
    totalEngagements: 308,
    articles: 95,
    videos: 3,
    stories: 8,
    matchCenter: 202,
  },
  mostActiveMonth: { month: "APRIL", matchCenterCount: 64 },
  favoriteClub: {
    clubId: "DFL-CLU-00000G",
    threeLetterCode: "FCB",
    clubName: "FC Bayern München",
    shortName: "Bayern",
    primaryColor: "#FF003C",
    secondaryColor: "#FFFFFF",
    trophies: [
      { count: 33, label: "BUNDESLIGA", icon: "🛡️" },
      { count: 20, label: "DFB-POKAL", icon: "🏆" },
      { count: 6, label: "CHAMPIONS LEAGUE", icon: "🏆" },
      { count: 2, label: "UEFA SUPER CUP", icon: "🥈" },
      { count: 2, label: "FIFA CLUB WORLD CUP", icon: "🏅" },
    ],
    motto: { native: "Mia san mia.", english: "We are who we are." },
  },
  topPlayer: {
    playerName: "Harry Edward Kane",
    goals: 26,
    assists: 8,
    clubId: "DFL-CLU-00000G",
    clubName: "FC Bayern München",
    clubShortName: "Bayern",
  },
  seasonHighlight: {
    homeTeam: "FC Bayern München",
    guestTeam: "Eintracht Frankfurt",
    result: "2:0",
    spectators: 81365,
    matchDay: 1,
    homeTeamId: "DFL-CLU-00000G",
    guestTeamId: "DFL-CLU-00000F",
  },
  narrative: {
    text: "You're a true Match Center Addict — 202 visits don't lie! With FC Bayern in your heart and Kane leading with 26 goals, April was your month to shine. What a Bundesliga season!",
  },
};

export const FAN_TYPES: Record<
  string,
  { emoji: string; color: string; desc: string }
> = {
  "Match Center Addict": { emoji: "📊", color: "#D4001A", desc: "You live for the data." },
  "Stats Nerd": { emoji: "🎬", color: "#C9A84C", desc: "Never miss a highlight." },
  "Story Lover": { emoji: "📖", color: "#4A9FD4", desc: "You follow every storyline." },
  "Casual Fan": { emoji: "⚽", color: "#6BCB77", desc: "You enjoy the beautiful game." },
};

export function fanTypeMeta(type: string) {
  return FAN_TYPES[type] ?? FAN_TYPES["Casual Fan"];
}

export const MONTHS = ["AUG","SEP","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY"];