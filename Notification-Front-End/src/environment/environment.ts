export interface AppEnvironment {
  apiBaseUrl: string;
}

const developmentEnvironment: AppEnvironment = {
  apiBaseUrl: 'http://localhost:8080/api/v1',
};

const productionEnvironment: AppEnvironment = {
  apiBaseUrl: '/api/v1',
};

export function getEnvironment(): AppEnvironment {
  return process.env.NODE_ENV === 'production' ? productionEnvironment : developmentEnvironment;
}
