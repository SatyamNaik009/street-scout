"use client";

import React, {ReactNode, useState} from 'react';
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

interface Props {
  children: ReactNode;
}

export default function ReactQueryProvider ({children }: Props) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            refetchOnWindowFocus: false,
            staleTime: 1000 * 5,
            gcTime: 1000 * 60 * 5,
            throwOnError: true,
          },

          mutations: {
            throwOnError: true,
          },
        },
      }),
  );
  return (
    <QueryClientProvider client={queryClient}>
      {children}
    </QueryClientProvider>
  );
}
