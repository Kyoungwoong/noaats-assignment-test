export type EvScenario = {
  label: string;
  probability: number;
  rewardType: "CASH" | "POINT" | "PERCENT";
  rewardValue: number;
};

export type EvRequest = {
  baseAmount: number;
  scenarios: EvScenario[];
};

export type EvScenarioResult = {
  label: string;
  probability: number;
  normalizedReward: number;
  expectedValue: number;
};

export type EvResponse = {
  expectedValue: number;
  expectedDiscountRate: number;
  scenarios: EvScenarioResult[];
};
